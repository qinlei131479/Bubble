package com.bubblecloud.codegen.config;

import cn.smallbun.screw.core.constant.DefaultConstants;
import lombok.Data;
import org.anyline.util.ConfigTable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成默认配置类
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = CodeGenDefaultProperties.PREFIX)
public class CodeGenDefaultProperties implements InitializingBean {

	public static final String PREFIX = "codegen";

	/**
	 * 是否开启在线更新
	 */
	private boolean autoCheckVersion = true;

	/**
	 * 在线模板分支
	 */
	private String branch = "master";

	/**
	 * 模板项目地址
	 */
	private String onlineUrl = DefaultConstants.CGTM_URL;

	/**
	 * 生成代码的包名
	 */
	private String packageName = "com.bubblecloud.biz";

	/**
	 * 生成代码的包名
	 */
	private String packageCommonName = "com.bubblecloud";


	/**
	 * 生成代码的包名
	 */
	private String packageEntityName = "com.bubblecloud.agi";

	/**
	 * 生成代码的版本
	 */
	private String version = "1.0.0";

	/**
	 * 生成代码的模块名
	 */
	private String moduleName = "agi";

	/**
	 * 生成代码的后端路径
	 */
	private String backendPath = "BubbleCloud";

	/**
	 * 生成代码的前端路径
	 */
	private String frontendPath = "AgetBubbles";

	/**
	 * 生成文件和校验路径允许写入的根目录。为空时使用服务工作目录所在的
	 * 最近Git仓库根目录。
	 */
	private List<String> allowedOutputRoots = new ArrayList<>();

	/**
	 * 生成代码的作者
	 */
	private String author = "Rampart";

	/**
	 * 生成代码的邮箱
	 */
	private String email = "";

	/**
	 * 表单布局（一列、两列）
	 */
	private Integer formLayout = 2;

	/**
	 * 下载方式 （0 文件下载、1写入目录）
	 */
	private String generatorType = "0";

	/**
	 * 是否同步路由
	 */
	private String syncRoute = "0";

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigTable.KEEP_ADAPTER = 0;
	}

}
