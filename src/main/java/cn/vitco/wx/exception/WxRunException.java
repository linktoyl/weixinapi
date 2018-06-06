package cn.vitco.wx.exception;

/**
 * 微信接口异常
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxRunException extends WxException {

    private static final long serialVersionUID = 2372915232825280617L;

    public WxRunException() {
    }

    public WxRunException(String message) {
        super(message);
    }

    public WxRunException(Throwable cause) {
        super(cause);
    }

    public WxRunException(String message, Throwable cause) {
        super(message, cause);
    }

}
