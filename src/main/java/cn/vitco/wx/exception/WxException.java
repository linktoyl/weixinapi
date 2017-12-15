package cn.vitco.wx.exception;

/**
 * 微信接口异常
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxException extends RuntimeException {

    private static final long serialVersionUID = 2372915232825280617L;

    public WxException() {
    }

    public WxException(String message) {
        super(message);
    }

    public WxException(Throwable cause) {
        super(cause);
    }

    public WxException(String message, Throwable cause) {
        super(message, cause);
    }

}
