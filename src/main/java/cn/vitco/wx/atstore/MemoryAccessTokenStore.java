package cn.vitco.wx.atstore;

import cn.vitco.wx.api.WxAccessTokenStoreApi;
import cn.vitco.wx.entity.WxAccessToken;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;


/**
 * 微信AccessToken 内存仓库
 * Created by Sterling on 2017/12/11.
 */
public class MemoryAccessTokenStore implements WxAccessTokenStoreApi {

    private static final Logger log = Logger.getLogger("wxLog");

    WxAccessToken at;

    public WxAccessToken get() {
        return at;
    }

    public void save(String token, int time, long lastCacheTimeMillis) {
        at = new WxAccessToken();
        at.setToken(token);
        at.setExpires(time);
        at.setLastCacheTimeMillis(lastCacheTimeMillis);
        log.debug("新获取到一个微信access_token: \n " + JSONObject.toJSON(at));
    }

}
