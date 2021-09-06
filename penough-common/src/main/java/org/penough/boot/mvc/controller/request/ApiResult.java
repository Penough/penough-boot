package org.penough.boot.mvc.controller.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.penough.boot.core.exceptions.BaseBusinessException;
import org.penough.boot.core.exceptions.code.ExceptionCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class ApiResult<T> {

	public static final int DEFAULT_STATUS_SUCCESS = 0;
	public static final String DEFAULT_CODE_SUCCESS = "0";

	public static final int DEFAULT_STATUS_FAILED = -1;
	public static final String DEFAULT_CODE_FAILED = "-1";

	// 统一状态编码
	@NotNull
	@ApiModelProperty(value = "状态码")
	private Integer status;

	// 返回信息描述
	@ApiModelProperty(value = "返回信息描述")
	private String message;

	// 错误信息编码，后台进行统一编码
	@ApiModelProperty(value = "错误信息编码")
	private String code;

	// 后台信息数据返回
	@ApiModelProperty(value = "后台信息数据返回")
	private T result;

	@JsonIgnore
	private Boolean defExec = true;

	@ApiModelProperty(value = "请求路径")
	private String path;

	/**
	 * 响应时间
	 */
	@ApiModelProperty(value = "响应时间戳")
	private long timestamp = System.currentTimeMillis();

	/**
	 * @param status
	 * @param code
	 */
	public ApiResult(Integer status, @NotNull String code) {
		super();
		this.status = status;
		this.code = code;
	}

	public ApiResult(Integer status, @NotNull String code, String message) {
		super();
		this.status = status;
		this.message = message;
		this.code = code;
	}



	public ApiResult(Integer status, @NotNull String code, String message, T result) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public static ApiResult SUCCESS() {
		ApiResult apiResult = new ApiResult(DEFAULT_STATUS_SUCCESS,"ok");
		return apiResult;
	}

	/**
	 * 请求成功消息
	 *
	 * @param data 结果
	 * @return RPC调用结果
	 */
	public static <E> ApiResult<E> SUCCESS(E data) {
		return new ApiResult<>(DEFAULT_STATUS_SUCCESS, DEFAULT_CODE_SUCCESS, "ok", data);
	}

	public static ApiResult FAILED() {
		return new ApiResult(DEFAULT_STATUS_FAILED,"");
	}

	public static <E> ApiResult<E> FAILED(E data) {
		return new ApiResult<>(DEFAULT_STATUS_FAILED, DEFAULT_CODE_FAILED, "failed", data);
	}

	public static <E> ApiResult<E> FAILED(String msg, E data) {
		return new ApiResult<>(DEFAULT_STATUS_FAILED, DEFAULT_CODE_FAILED, msg, data);
	}

	public static <E> ApiResult<E> FAILED(String code, String msg, E data) {
		return new ApiResult<>(DEFAULT_STATUS_FAILED, code, msg, data);
	}

	public static <E> ApiResult<E> FAILED(Integer status, String code, String msg, E data) {
		return new ApiResult<>(status, code, msg, data);
	}

	public static ApiResult INTERNAL_ERROR() {
		ApiResult apiResult = new ApiResult(3,"");
		return apiResult;
	}

	public static <E> ApiResult<E> result(int status, String code, E data, String msg) {
		return new ApiResult<>(status, code, msg, data);
	}

	public static <E> ApiResult<E> result(BaseBusinessException ex, E data) {
		return new ApiResult<>(ex.getStatus(), ex.getCode(), ex.getMessage(), data);
	}

	public static <E> ApiResult<E> result(ExceptionCode ex, E data) {
		return new ApiResult<>(ex.getStatus(), ex.getCode(), ex.getMsg(), data);
	}

	public static <E> ApiResult<E> result(ExceptionCode ex, String msg, E data) {
		return new ApiResult<>(ex.getStatus(), ex.getCode(), msg, data);
	}
	
	/**
     * @param result
     */
    public void setResult(T result) {
        this.result = result;
    }

    public void setErrMsg(@NotNull String code, String message){
    	this.code = code;
    	this.message = message;
	}

	public static <E> ApiResult<E> successDef(E data) {
		return new ApiResult<>(DEFAULT_STATUS_SUCCESS, DEFAULT_CODE_SUCCESS, "ok", data);
	}

	public static <E> ApiResult<E> successDef() {
		return new ApiResult<>(DEFAULT_STATUS_SUCCESS, DEFAULT_CODE_SUCCESS, "ok", null);
	}
}
