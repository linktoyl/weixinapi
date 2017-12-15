package cn.vitco.wx.entity;

/**
 *
 * 微信AccessTiken 实体信息类
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxAccessToken {

    protected String token;

    protected int expires;

    protected long lastCacheTimeMillis;

    /**
     *
     */
    public WxAccessToken() {
    }

    /**
     * @param token
     * @param expires
     * @param lastCacheTimeMillis
     */
    public WxAccessToken(String token, int expires, long lastCacheTimeMillis) {
        this.token = token;
        this.expires = expires;
        this.lastCacheTimeMillis = lastCacheTimeMillis;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
