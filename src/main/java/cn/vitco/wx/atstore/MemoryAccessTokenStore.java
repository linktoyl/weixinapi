package cn.vitco.wx.atstore;

import cn.vitco.wx.api.WxAccessTokenStoreApi;
import cn.vitco.wx.entity.WxAccessToken;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信AccessToken 内存仓库
 * Created by Sterling on 2017/12/11.
 */
public class MemoryAccessTokenStore implements WxAccessTokenStoreApi {

    private static final Logger log = Logger.getLogger("wxLog");

    Map<String, WxAccessToken> tokenStore = new HashMap<String, WxAccessToken>();


    public WxAccessToken get(String appid) {
        return tokenStore.get(appid);
    }

    public void save(String appid, String token, int time, long lastCacheTimeMillis) {
        WxAccessToken at = new WxAccessToken();
        at.setToken(token);
        at.setExpires(time);
        at.setLastCacheTimeMillis(lastCacheTimeMillis);
        tokenStore.put(appid, at);
        log.debug("新获取到一个微信access_token: \n " + JSONObject.toJSON(at));
    }

}
