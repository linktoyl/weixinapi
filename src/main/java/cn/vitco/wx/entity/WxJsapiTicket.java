package cn.vitco.wx.entity;

/**
 *
 * 微信JsapiTicket 实体信息类
 * Created by Sterling on 2017/12/11.
 */
public class WxJsapiTicket {

    protected String ticket;

    protected int expires;

    protected long lastCacheTimeMillis;



    public WxJsapiTicket() {
    }

    public WxJsapiTicket(String ticket, int expires, long lastCacheTimeMillis) {
        this.ticket = ticket;
        this.expires = expires;
        this.lastCacheTimeMillis = lastCacheTimeMillis;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public long getLastCacheTimeMillis() {
        return lastCacheTimeMillis;
    }

    public void setLastCacheTimeMillis(long lastCacheTimeMillis) {
        this.lastCacheTimeMillis = lastCacheTimeMillis;
    }
}
