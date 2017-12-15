package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxJsapiTicket;

/**
 * Created by Sterling on 2017/12/11.
 */
public interface WxJsapiTicketStoreApi {
    WxJsapiTicket get();

    void save(String ticket, int expires, long lastCacheTimeMillis);

}
