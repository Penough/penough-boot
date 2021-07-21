package org.penough.boot.log.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.penough.boot.core.exceptions.BaseBusinessException;
import org.penough.boot.core.exceptions.code.BaseExceptionCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Penough
 * @createTime 2017-12-13 10:55
 */
@Getter
@Setter
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class ResponseEntity<T> {
    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";
    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = -1;
    public static final int TIMEOUT_CODE = -2;
    /**
     * 统一参数验证异常
     */
    public static final int VALID_EX_CODE = -9;
    public static final int OPERATION_EX_CODE = -10;
    /**
     * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    @ApiModelProperty(value = "响应编码:0/200-请求处理成功")
    private int code;

    /**
     * 是否执行默认操作
     */
    @JsonIgnore
    private Boolean defExec = true;

    /**
     * 调用结果
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 结果消息，如果调用成功，消息通常为空T
     */
    @ApiModelProperty(value = "提示消息")
    private String msg = "ok";

    @ApiModelProperty(value = "请求路径")
    private String path;
    /**
     * 附加数据
     */
    @ApiModelProperty(value = "附加数据")
    private Map<String, Object> extra;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间戳")
    private long timestamp = System.currentTimeMillis();

    private ResponseEntity() {
        super();
    }

    public ResponseEntity(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = false;
    }

    public ResponseEntity(int code, T data, String msg, boolean defExec) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = defExec;
    }

    public static <E> ResponseEntity<E> result(int code, E data, String msg) {
        return new ResponseEntity<>(code, data, msg);
    }

    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> success(E data) {
        return new ResponseEntity<>(SUCCESS_CODE, data, "ok");
    }

    public static ResponseEntity<Boolean> success() {
        return new ResponseEntity<>(SUCCESS_CODE, true, "ok");
    }


    public static <E> ResponseEntity<E> successDef(E data) {
        return new ResponseEntity<>(SUCCESS_CODE, data, "ok", true);
    }

    public static <E> ResponseEntity<E> successDef() {
        return new ResponseEntity<>(SUCCESS_CODE, null, "ok", true);
    }

    public static <E> ResponseEntity<E> successDef(E data, String msg) {
        return new ResponseEntity<>(SUCCESS_CODE, data, msg, true);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> success(E data, String msg) {
        return new ResponseEntity<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> ResponseEntity<E> fail(int code, String msg) {
        return new ResponseEntity<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> ResponseEntity<E> fail(String msg) {
        return fail(OPERATION_EX_CODE, msg);
    }

    public static <E> ResponseEntity<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new ResponseEntity<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> ResponseEntity<E> fail(BaseExceptionCode exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> ResponseEntity<E> fail(BaseBusinessException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new ResponseEntity<>(exception.getCode(), null, exception.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> fail(Throwable throwable) {
        return fail(FAIL_CODE, throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE);
    }

    public static <E> ResponseEntity<E> validFail(String msg) {
        return new ResponseEntity<>(VALID_EX_CODE, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> ResponseEntity<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new ResponseEntity<>(VALID_EX_CODE, null, String.format(message, args));
    }

    public static <E> ResponseEntity<E> validFail(BaseExceptionCode exceptionCode) {
        return new ResponseEntity<>(exceptionCode.getCode(), null,
                (exceptionCode.getMsg() == null || exceptionCode.getMsg().isEmpty()) ? DEF_ERROR_MESSAGE : exceptionCode.getMsg());
    }

    public static <E> ResponseEntity<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }


    public ResponseEntity<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(10);
        }
        this.extra.put(key, value);
        return this;
    }

    /**
     * 逻辑处理是否成功
     *
     * @return 是否成功
     */
    public Boolean getIsSuccess() {
        return this.code == SUCCESS_CODE || this.code == 200;
    }

    /**
     * 逻辑处理是否失败
     *
     * @return
     */
    public Boolean getIsError() {
        return !getIsSuccess();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
