package cn.vitco.wx.atstore;

import cn.vitco.wx.api.WxJsapiTicketStoreApi;
import cn.vitco.wx.entity.WxJsapiTicket;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信JsapiTicket 内存仓库
 * Created by Sterling on 2017/12/11.
 */
public class MemoryJsapiTicketStore implements WxJsapiTicketStoreApi {

    Map<String, WxJsapiTicket> ticketMap = new HashMap<String, WxJsapiTicket>();

    public WxJsapiTicket get(String appid) {
        return ticketMap.get(appid);
    }

    public void save(String appid, String ticket, int expires, long lastCacheTimeMillis) {
        WxJsapiTicket jt = new WxJsapiTicket(ticket, expires, lastCacheTimeMillis);
        ticketMap.put(appid, jt);
    }

}
