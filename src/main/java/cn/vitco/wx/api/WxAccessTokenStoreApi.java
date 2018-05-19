package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxAccessToken;

/**
 * Created by Sterling on 2017/12/11.
 */
public interface WxAccessTokenStoreApi {
    WxAccessToken get(String appid);

    void save(String appid, String token, int expires, long lastCacheTimeMillis);
}
