package com.bubblecloud.common.log.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bubblecloud.backend.api.dto.SysLogDTO;
import com.bubblecloud.backend.api.feign.RemoteLogService;
import com.bubblecloud.common.core.jackson.CustomJavaTimeModule;
import com.bubblecloud.common.log.config.CustomLogProperties;
import com.bubblecloud.common.log.util.JacksonSensitiveFieldUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author lengleng 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener implements InitializingBean {

	/**
	 * 忽略序列化的对象类型
	 */
	private static final Class<?>[] IGNORE_CLASSES = { ServletRequest.class, ServletResponse.class, BindingResult.class,
			MultipartFile.class, Part.class, InputStream.class, OutputStream.class, Reader.class, Writer.class,
			Resource.class, File.class, Path.class };

	private final RemoteLogService remoteLogService;

	private final CustomLogProperties logProperties;

	@SneakyThrows
	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLogDTO source = (SysLogDTO) event.getSource();

		// json 格式刷参数放在异步中处理，提升性能
		if (Objects.nonNull(source.getBody()) && logProperties.isRequestEnabled()) {
			Object[] args = (Object[]) source.getBody();
			List<Object> list = CollUtil.toList(args);
			// 删除部分无法序列化的参数
			list.removeIf(this::isIgnoredParameter);

			try {
				// 序列化参数
				String params = JacksonSensitiveFieldUtil.getObjectMapper().writeValueAsString(list);
				source.setParams(StrUtil.subPre(params, logProperties.getMaxLength()));
			}
			catch (Exception e) {
				log.error("请求参数序列化异常:{}", e.getMessage());
			}
		}

		source.setBody(null);
		remoteLogService.saveLog(source);
	}

	/**
	 * 判断请求参数是否属于不可异步序列化的类型
	 * <p>
	 * 请求结束后 Tomcat 会清理 multipart 临时文件，流、文件类参数在异步线程中序列化会触发 NoSuchFileException，需要在序列化前剔除。
	 * @param obj 请求参数，可能为 null（null 可正常序列化，予以保留）
	 * @return true 表示需要从序列化列表中剔除
	 */
	private boolean isIgnoredParameter(Object obj) {
		return Objects.nonNull(obj)
				&& Arrays.stream(IGNORE_CLASSES).anyMatch(clazz -> clazz.isAssignableFrom(obj.getClass()));
	}

	@Override
	public void afterPropertiesSet() {
		String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);
		JacksonSensitiveFieldUtil.configureSensitiveFields(ignorableFieldNames);
		JacksonSensitiveFieldUtil.registerCustomModule(new CustomJavaTimeModule());
	}

}
