package org.penough.boot.log.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.penough.boot.core.code.BaseCode;
import org.penough.boot.core.code.BusinessCode;
import org.penough.boot.core.code.ExceptionCode;
import org.penough.boot.core.exceptions.BaseBusinessException;
import org.penough.boot.core.utils.JsonUtil;

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
    public static final int TIMEOUT_CODE = -2;
    /**
     * 状态码,RFC规范
     */
    @ApiModelProperty(value = "状态码,RFC规范")
    private int status;
    /**
     * 自定义业务码
     */
    @ApiModelProperty(value = "自定义业务码")
    private String code;

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

    public ResponseEntity(int status, String code, T data, String msg) {
        this.status = status;
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = false;
    }

    public ResponseEntity(int status, String code, T data, String msg, boolean defExec) {
        this.status = status;
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.defExec = defExec;
    }

    public ResponseEntity(BaseCode baseCode, T data) {
        this.status = baseCode.getStatus();
        this.code = baseCode.getCode();
        this.msg = baseCode.getMsg();
        this.data = data;
    }

    public ResponseEntity(BaseCode baseCode, T data, String msg) {
        this.status = baseCode.getStatus();
        this.code = baseCode.getCode();
        this.msg = msg;
        this.data = data;
    }

    public ResponseEntity(BaseCode baseCode, T data, boolean defExec) {
        this.status = baseCode.getStatus();
        this.code = baseCode.getCode();
        this.msg = baseCode.getMsg();
        this.data = data;
        this.defExec = defExec;
    }

    public static <E> ResponseEntity<E> result(int status, String code, E data, String msg) {
        return new ResponseEntity<>(status, code, data, msg);
    }



    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> success(E data) {
        return new ResponseEntity<>(BusinessCode.SUCCESS, data);
    }

    public static ResponseEntity<Boolean> success() {
        return new ResponseEntity<>(BusinessCode.SUCCESS, true);
    }


    public static <E> ResponseEntity<E> successDef(E data) {
        return new ResponseEntity<>(BusinessCode.SUCCESS, data, true);
    }

    public static <E> ResponseEntity<E> successDef() {
        return new ResponseEntity<>(BusinessCode.SUCCESS, null, true);
    }

    public static <E> ResponseEntity<E> successDef(E data, String msg) {
        return new ResponseEntity<>(BusinessCode.SUCCESS.getStatus(),
                BusinessCode.SUCCESS.getCode(), data, msg, true);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> success(E data, String msg) {
        return new ResponseEntity<>(BusinessCode.SUCCESS, data, msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> ResponseEntity<E> fail(ExceptionCode code, String msg) {
        return new ResponseEntity<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> ResponseEntity<E> fail(String msg) {
        return fail(ExceptionCode.SYSTEM_EXCEPTION, msg);
    }

    public static <E> ResponseEntity<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new ResponseEntity<>(ExceptionCode.SYSTEM_EXCEPTION, null, String.format(message, args));
    }

    public static <E> ResponseEntity<E> fail(BaseCode exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> ResponseEntity<E> fail(BaseBusinessException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new ResponseEntity<>(exception.getStatus(), exception.getCode(), null, exception.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> ResponseEntity<E> fail(Throwable throwable) {
        return fail(ExceptionCode.FAILED, throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE);
    }

    public static <E> ResponseEntity<E> validFail(String msg) {
        return new ResponseEntity<>(ExceptionCode.BASE_VALID_PARAM, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> ResponseEntity<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new ResponseEntity<>(ExceptionCode.BASE_VALID_PARAM, null, String.format(message, args));
    }

    public static <E> ResponseEntity<E> validFail(BaseCode exceptionCode) {
        return new ResponseEntity<>(exceptionCode.getStatus(), exceptionCode.getCode(), null,
                (exceptionCode.getMsg() == null || exceptionCode.getMsg().isEmpty()) ? DEF_ERROR_MESSAGE : exceptionCode.getMsg());
    }

    public static <E> ResponseEntity<E> timeout() {
        return fail(ExceptionCode.SERVICE_TIME_OUT);
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
        return this.code == BusinessCode.SUCCESS.getCode() || this.status == BusinessCode.SUCCESS.getStatus();
    }

    /**
     * 逻辑处理是否失败
     *
     * @return
     */
    public Boolean getIsError() {
        return !getIsSuccess();
    }

    @SneakyThrows
    @Override
    public String toString() {
        return JsonUtil.parseJsonString(this);
    }
}
