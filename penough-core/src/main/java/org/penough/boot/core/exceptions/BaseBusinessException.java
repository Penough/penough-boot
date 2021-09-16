package org.penough.boot.core.exceptions;


import org.penough.boot.core.constants.Constants;
import org.penough.boot.core.code.BaseCode;

/**
 * 自定义业务异常类（强化）
 *
 * @author Penoughs
 * @date 2020/10/22
 */
public class BaseBusinessException extends CustomException {

    public BaseBusinessException(String message){
        super(Constants.FAILED_STATUS, Constants.FAILED_CODE, message);
    }

    public BaseBusinessException(String code, String message){
        super(code, message);
    }

    public BaseBusinessException(String code, String format, Object... args){
        super(code, format, args);
    }

    public BaseBusinessException(int status, String code, String message){
        super(status, code, message);
    }

    public BaseBusinessException(int status, String code, String format, Object... args){
        super(status, code, format, args);
    }

    /**
     * 实例化异常
     */
    public static BaseBusinessException wrap(int status, String code, String format, Object... args){
        return new BaseBusinessException(status, code, format, args);
    }

    public static BaseBusinessException wrap(String code, String msg){
        return new BaseBusinessException(code, msg);
    }

    public static BaseBusinessException wrap(int status, String code, String msg){
        return new BaseBusinessException(status, code, msg);
    }

    public static BaseBusinessException wrap(BaseCode ec){
        return new BaseBusinessException(ec.getStatus(), ec.getCode(), ec.getMsg());
    }

    @Override
    public String toString(){
        return "BaseBusinessException [status="+ status + "message=" + message + ", code=" + code + "]";
    }
}
