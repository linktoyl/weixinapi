package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxAccessToken;

/**
 * Created by Sterling on 2017/12/11.
 */
public interface WxAccessTokenStoreApi {
    WxAccessToken get();

    void save(String token, int expires, long lastCacheTimeMillis);
}
