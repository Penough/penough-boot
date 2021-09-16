package org.penough.boot.core.code;

import org.penough.boot.core.constants.Constants;

/**
 * 业务码码枚举类<br>
 * 200段 SUCCESS部分<br>
 * RFC-HTTP状态码未定义部分<br>
 * @author Penough
 * @date 2020/11/25
 */
public enum BusinessCode implements BaseCode {

    SUCCESS("success", "ok"),
    ;
    private int status = Constants.SUCCESS_STATUS; // 状态码默认200
    private String code = Constants.SUCCESS_CODE; // 编码默认系统内部错误
    private String msg;

    BusinessCode(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    BusinessCode(String code, String msg){
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
