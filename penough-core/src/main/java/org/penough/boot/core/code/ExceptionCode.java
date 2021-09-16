package org.penough.boot.core.code;

import org.penough.boot.core.constants.Constants;

/**
 * 异常码枚举类<br>
 * RFC-已定义异常部分<br>
 * 大于300<br>
 * @author Penough
 * @date 2020/11/25
 */
public enum ExceptionCode implements BaseCode {

    SYSTEM_EXCEPTION("sys-internal-exception", "系统异常~请联系管理员排查~"),

    FAILED("failed", "业务处理失败，需排查处理..."),
    TOO_MANY_DATA_ERROR("too-many-datas", "数据过多！"),
    SUPER_MAPPER_TRERROR("super-mapper-trerror", "SuperMapper类型转换异常"),
    NULL_POINT_EX("null-pointer", "空指针异常"),
    SQL_EX("sql_ex", "运行SQL出现异常"),
    SYSTEM_BUSY("system-busy", "系统繁忙,请稍后再试"),
    SERVICE_TIME_OUT("service-time-out", "服务处理超时..."),

    PARAM_EX(400,"param-solve-excetpion", "参数类型解析异常"),
    ILLEGALA_ARGUMENT_EX(400, "invalid-params", "无效参数异常"),
    MEDIA_TYPE_EX(400, "req-type-exception", "请求类型异常"),
    REQUIRED_FILE_PARAM_EX(400, "file-required", "请求中必须至少包含一个有效文件"),
    BASE_VALID_PARAM(400, "base_valid_param", "统一验证参数异常"),


    METHOD_NOT_ALLOWED(405, "method_not_allowed", "不支持当前请求类型"),
    ;
    private int status = Constants.FAILED_STATUS; // 状态码默认500
    private String code = Constants.FAILED_CODE; // 编码默认系统内部错误
    private String msg;

    ExceptionCode(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    ExceptionCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static BaseCode matchByCode(String code) {
        for (BaseCode i: values()) {
            if(i.getCode().equals(code)) return i;
        }
        return null;
    }
}
