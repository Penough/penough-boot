package org.penough.boot.core.exceptions;

/**
 * 自定义异常类
 *
 * @author Penough
 * @date 2020/10/22
 */
public class CustomException extends RuntimeException implements BaseException {

    protected String message;
    protected String code;
    protected int status;

    public CustomException(int status, String code, String message){
        super(message);
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public CustomException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public CustomException(String code, String format, Object... args){
        super(String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }

    public CustomException(int status, String code, String format, Object... args){
        super(String.format(format, args));
        this.status = status;
        this.message = String.format(format, args);
        this.code = code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
