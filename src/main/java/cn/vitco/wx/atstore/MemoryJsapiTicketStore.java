package cn.vitco.wx.atstore;

import cn.vitco.wx.api.WxJsapiTicketStoreApi;
import cn.vitco.wx.entity.WxJsapiTicket;

/**
 * 微信JsapiTicket 内存仓库
 * Created by Sterling on 2017/12/11.
 */
public class MemoryJsapiTicketStore implements WxJsapiTicketStoreApi {

    WxJsapiTicket jt;

    public WxJsapiTicket get() {
        return jt;
    }

    public void save(String ticket, int expires, long lastCacheTimeMillis) {
        jt = new WxJsapiTicket(ticket, expires, lastCacheTimeMillis);
    }

}
