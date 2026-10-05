package com.bubblecloud.common.core.util;

import com.bubblecloud.common.core.constant.CommonConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "响应信息主体")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@JsonPropertyDescription(value = "调用成功返回0，调用失败返回1")
	@Schema(description = "返回标记：成功标记=0，失败标记=1")
	private int code;

	@Getter
	@Setter
	@JsonPropertyDescription(value = "返回具体的信息")
	@Schema(description = "返回信息")
	private String msg;

	@Getter
	@Setter
	@Schema(description = "数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg);
	}

	/**
	 * 与 OA 原 PhpResponse 成功形态一致：msg 固定为 ok，业务载荷在 data。
	 */
	public static <T> R<T> phpOk(T data) {
		return restResult(data, CommonConstants.SUCCESS, "ok");
	}

	/**
	 * 与 OA 原 PhpResponse 失败形态一致。
	 */
	public static <T> R<T> phpFailed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public boolean isOk() {
		return this.code == CommonConstants.SUCCESS;
	}

	/**
	 * OA / PHP 前端兼容：HTTP 语义状态（200 成功 / 400 失败），与 code（0/1）并行序列化。
	 */
	@JsonProperty(value = "status", access = JsonProperty.Access.READ_ONLY)
	public Integer getPhpHttpStatus() {
		if (code == CommonConstants.SUCCESS) {
			return 200;
		}
		if (code == CommonConstants.FAIL) {
			return 400;
		}
		return null;
	}

}
