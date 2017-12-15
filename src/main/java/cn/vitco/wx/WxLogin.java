package cn.vitco.wx;

import cn.vitco.wx.api.WxLoginApi;
import cn.vitco.wx.config.WX_API_URL;
import cn.vitco.wx.entity.WxResContent;
import cn.vitco.wx.http.WxRequest;
import cn.vitco.wx.http.WxResponse;
import cn.vitco.wx.util.WxMap;
import org.apache.http.HttpException;

/**
 * Created by Sterling on 2017/12/12.
 */
public class WxLogin implements WxLoginApi {

    protected String host;
    protected String appid;
    protected String appsecret;

    public WxLogin(String appid, String appsecret) {
        this.appid = appid;
        this.appsecret = appsecret;
    }

    public WxResContent access_token(String auth_code) throws HttpException {
        WxRequest req = new WxRequest(WX_API_URL.WX_OAUTH_ACCESSTOKEN, WxRequest.METHOD.GET);
        WxMap params = new WxMap();
        params.put("appid", appid);
        params.put("secret", appsecret);
        params.put("code", auth_code);
        params.put("grant_type", "authorization_code");
        req.setParams(params);
        WxResponse resp = req.send();
        if (!resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    public WxResContent refresh_token(String refresh_token) {
        // TODO Auto-generated method stub
        return null;
    }

    public WxResContent auth(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    public WxResContent userinfo(String openid, String token) {
        WxRequest req = new WxRequest(WX_API_URL.WX_OAUTH_USERINFO, WxRequest.METHOD.GET);
        WxMap params = new WxMap();
        params.put("access_token", token);
        params.put("openid", openid);
        req.setParams(params);
        req.setParams(params);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }
}
