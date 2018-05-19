package cn.vitco.wx.api;

import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;

/**
 * JsapiTicket 相关接口
 * Created by Sterling on 2017/12/11.
 */
public interface WxJsapiTicketApi {
    //void setJsapiTicketStore(WxJsapiTicketStore ats);


    String getJsapiTicket(String appid) throws WxException;

    WxMap genJsSDKConfig(String appid, String url) throws WxException;
}