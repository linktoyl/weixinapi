package cn.vitco.wx.exception;

/**
 * 微信API接口调用返回异常
 */
public class WxResException extends Exception {
    private String errCode;
    private String errMsg;
    private String bussMsg;

    public WxResException(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WxResException(String errCode, String errMsg, String bussMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.bussMsg = bussMsg;
    }


    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getBussMsg() {
        return bussMsg;
    }

    @Override
    public String getMessage() {
        return "{errCode='" + errCode + '\'' +
                        ", errMsg='" + errMsg + '\'' +
                        ", bussMsg='" + bussMsg + '\'' +
                        '}';
    }

    @Override
    public String toString() {
        return "WxResException{errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", bussMsg='" + bussMsg + '\'' +
                '}';
    }
}
