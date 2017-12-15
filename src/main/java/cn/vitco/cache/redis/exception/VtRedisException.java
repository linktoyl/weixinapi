package cn.vitco.cache.redis.exception;

/**
 * redis 自定义异常类
 * Created by Sterling on 2017/12/12.
 */
public class VtRedisException extends RuntimeException {
    private int errorCode = 0;
    private String msg = "";

    public VtRedisException() {
    }

    public VtRedisException(String message) {
        super(message);
        this.msg = message;
    }

    public VtRedisException(Throwable cause) {
        super(cause);
    }

    public VtRedisException(int code, String message) {
        super(message);
        this.errorCode = code;
        this.msg = message;
    }

    public VtRedisException(int code, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
        this.msg = message;
    }

    public VtRedisException(int code, Throwable cause) {
        super(cause);
        this.errorCode = code;
    }

    public VtRedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
