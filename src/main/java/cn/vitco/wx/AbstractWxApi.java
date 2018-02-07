package cn.vitco.wx;

import cn.vitco.cache.redis.JedisClusterFactory;
import cn.vitco.common.Lang;
import cn.vitco.common.Xmls;
import cn.vitco.wx.api.WxAccessTokenStoreApi;
import cn.vitco.wx.api.WxApi;
import cn.vitco.wx.api.WxJsapiTicketStoreApi;
import cn.vitco.wx.atstore.MemoryAccessTokenStore;
import cn.vitco.wx.atstore.MemoryJsapiTicketStore;
import cn.vitco.wx.atstore.RedisAccessTokenStore;
import cn.vitco.wx.atstore.RedisJsapiTicketStore;
import cn.vitco.wx.config.WX_API_CONFIG;
import cn.vitco.wx.config.WX_API_URL;
import cn.vitco.wx.entity.*;
import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.http.WxRequest;
import cn.vitco.wx.http.WxRequest.METHOD;
import cn.vitco.wx.http.WxResponse;
import cn.vitco.wx.util.WxMap;
import cn.vitco.wx.util.WxPaySSL;
import cn.vitco.wx.util.WxPaySign;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import java.util.UUID;

/**
 * Created by Sterling on 2017/12/11.
 */
public abstract class AbstractWxApi implements WxApi {

    private final static Logger log = Logger.getLogger("WxApi_Log");

    protected static WxAccessTokenStoreApi accessTokenStore;
    protected static WxJsapiTicketStoreApi jsapiTicketStore;

    protected Object lock = new Object();

    public AbstractWxApi() {
        //根据缓存模式创建缓存
        if(WX_API_CONFIG.getAccessTokenCache().equals("Memory")){
            accessTokenStore = new MemoryAccessTokenStore();
            jsapiTicketStore = new MemoryJsapiTicketStore();
        }else if(WX_API_CONFIG.getAccessTokenCache().equals("Redis")){
            JedisClusterFactory jcFactory = new JedisClusterFactory();
            accessTokenStore = new RedisAccessTokenStore(jcFactory.getJedisCluster());
            jsapiTicketStore = new RedisJsapiTicketStore(jcFactory.getJedisCluster());
        }
    }

    /**
     * 获取微信用户信息
     * @param openid
     * @return
     */
    @Override
    public WxResContent user_info(String openid) {
        WxResContent wxres = user_info_safe(openid);
        if(wxres.errcode()==40001){
            synchronized (lock) {
                try {
                    reflushAccessToken();
                } catch (IllegalArgumentException e) {
                    log.error("刷新JsapiTicket出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新JsapiTicket出错[网络问题]:"+e.getMessage());
                }
            }
            wxres = user_info_safe(openid);
        }
        return wxres;
    }

    /**
     *  微信小程序
     * 登录凭证 code 获取 session_key 和 openid
     * @param code
     * @return
     */
    public WxResContent wx_mini_login(String code){
        String url = String.format(WX_API_URL.WX_MINI_LOGIN, WX_API_CONFIG.getAppid(), WX_API_CONFIG.getAppsecret(), code);
        WxRequest req = new WxRequest(url, METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("微信小程序登录凭证 code 获取 session_key 和 openid异常:"+code, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));

    }

    private WxResContent user_info_safe(String openid){
        WxRequest req = new WxRequest(WX_API_URL.WX_GET_USERINFO, METHOD.GET);
        WxMap params = new WxMap();
        params.put("access_token", getAccessToken());
        params.put("openid", openid);
        req.setParams(params);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取用户信息异常:"+openid, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    /**
     * 发送红包
     * @param paykey
     * @param wxRedPack
     * @param certfile
     * @param password
     * @return
     */
    @Override
    public WxMap send_redpack(String paykey, WxRedPack wxRedPack, String certfile, String password)  throws WxException{
        Map<String, Object> params = Lang.objectToMap(wxRedPack);
        return this.postPay(WX_API_URL.WX_PAY_REDPACK, paykey, params, certfile, password);
    }

    /**
     * 查询红包发送状态
     * @param payKey
     * @param params
     * @param certfile
     * @param password
     * @return
     */
    @Override
    public WxMap query_redpackRecode(String payKey, Map<String, Object> params, String certfile, String password) throws WxException{
        return this.postPay(WX_API_URL.WX_QUERY_REDPACK, payKey, params, certfile, password);
    }

    /**
     * 企业付款接口
     * @param paykey
     * @param wxPayment
     * @param certfile
     * @param password
     * @return
     * @throws WxException
     */
    @Override
    public WxMap pay_transfers(String paykey, WxPayment wxPayment, String certfile, String password) throws WxException {
        Map<String, Object> params = Lang.objectToMap(wxPayment);
        return this.postPay(WX_API_URL.WX_PAY_TRANSFERS, paykey, params, certfile, password);
    }

    /**
     * 查询企业付款接口
     * @param paykey
     * @param params
     * @param certfile
     * @param password
     * @return
     * @throws WxException
     */
    @Override
    public WxMap query_transfers(String paykey, Map<String, Object> params, String certfile, String password) throws WxException {
        return this.postPay(WX_API_URL.WX_QUERY_TRANSFERS, paykey, params, certfile, password);
    }

    /**
     * 下载 微信媒体文件
     * @param accessToken
     * @param mediaId
     * @return
     */
    @Override
    public InputStream downMedia(String accessToken, String mediaId) {
        String url = String.format(WX_API_URL.WX_DOWNLOAD_MEDIA, accessToken, mediaId);
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 将mediaId作为文件名
            System.out.println("录音：" + http.getContentType());
            InputStream is = http.getInputStream();
            return is;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成JsSdk签名
     * @param url
     * @return
     */
    @Override
    public WxMap genJsSDKConfig(String url) throws WxException{
        String jt = this.getJsapiTicket();
        if(jt == null)
            throw new WxException("生成JsSdkConfig失败, JsapiTicket为null!");
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = UUID.randomUUID().toString();

        String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s", jt, nonceStr, timestamp, url);
        String signature = Lang.sha1(str);

        WxMap map = new WxMap();
        map.put("url", url);
        map.put("jsapi_ticket", jt);
        map.put("appid", WX_API_CONFIG.getAppid());
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);
        return map;
    }

    /**
     * 微信支付公共POST方法（带证书）
     *
     * @param url      请求路径
     * @param key      商户KEY
     * @param params   参数
     * @param certfilePath     证书文件
     * @param password 证书密码
     * @return
     */
    public WxMap postPay(String url, String key, Map<String, Object> params, String certfilePath, String password) throws WxException{
        File certfile = null;
        try {
            certfile = new File(certfilePath);
        } catch (Exception e) {
            throw new WxException("微信证书异常!");
        }
        params.remove("sign");
        String sign = WxPaySign.createSign(key, params);
        params.put("sign", sign);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.POST);
        String reqdata = Xmls.mapToXml(params, true);
        log.info("微信支付XMl:\n %s" + reqdata);
        req.setData(reqdata);
        SSLSocketFactory sslSocketFactory;
        try {
            sslSocketFactory = WxPaySSL.buildSSL(certfile, password);
            req.setSslSocketFactory(sslSocketFactory);
            WxResponse resp = req.send();
            if (!resp.isOK())
                throw new IllegalStateException("postPay with SSL, resp code=" + resp.getStatus());
            String respContent = resp.getContent("UTF-8");
            log.info("微信支付返回:\n " + respContent);
            return new WxMap(Xmls.xmlToMap(respContent,"xml"));
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }


    @Override
    public String getJsapiTicket() {
        WxJsapiTicket jt = jsapiTicketStore.get();
        if (jt == null) {
            synchronized (lock) {
                try {
                    reflushJsapiTicket();
                } catch (IllegalArgumentException e) {
                    log.error("刷新JsapiTicket出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新JsapiTicket出错[网络问题]:"+e.getMessage());
                }
                jt = jsapiTicketStore.get();
            }
        }
        if(jt == null){
            throw new WxException("获取JsApiTicket失败!");
        }
        return jt.getTicket();
    }

    @Override
    public String getAccessToken() {
        WxAccessToken at = accessTokenStore.get();
        if (at == null) {
            synchronized (lock) {
                try {
                    reflushAccessToken();
                } catch (IllegalArgumentException e) {
                    log.error("刷新AccessToken出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新AccessToken出错[网络问题]:"+e.getMessage());
                }
                at = accessTokenStore.get();
            }
        }
        if(at == null){
            throw new WxException("获取AccessToken失败!");
        }
        return at.getToken();
    }

    /**
     * 刷新JsApiTicket
     */
    protected synchronized void reflushJsapiTicket() throws IllegalArgumentException, HttpException {
        String accessToken = this.getAccessToken();

        String url = String.format(WX_API_URL.WX_GET_JSAPITICKET, accessToken);
        if (log.isDebugEnabled())
            log.debug("刷新jsapi ticket 地址: " + url);

        WxRequest req = new WxRequest(url, METHOD.GET);
        WxResponse resp =  req.send();

        if (resp == null || !resp.isOK())
            throw new IllegalArgumentException("刷新JsapiTicket失败!");
        String str = resp.getContent();

        if (log.isDebugEnabled())
            log.debug("刷新 jsapi ticket返回: " + str);

        WxResContent re = WxResContent.format(str);
        String ticket = re.getString("ticket");
        int expires = re.getInt("expires_in") - 200;// 微信默认超时为7200秒，此处设置稍微短一点
        jsapiTicketStore.save(ticket, expires, System.currentTimeMillis());
    }

    /**
     * 刷新AccessToken
     */
    protected synchronized void reflushAccessToken() throws IllegalArgumentException, HttpException{
        String url = String.format(WX_API_URL.WX_GET_ACCESSTOKEN, WX_API_CONFIG.getAppid(), WX_API_CONFIG.getAppsecret());
        if (log.isDebugEnabled())
            log.debug("刷新access_token 地址:" + url);

        WxResponse resp = new WxRequest(url, METHOD.GET).send();

        if (resp == null || !resp.isOK())
            throw new IllegalArgumentException("刷新AccessToken失败!");
        String str = resp.getContent();
        if (log.isDebugEnabled())
            log.debug("刷新access_token 返回: " + str);

        WxResContent re = WxResContent.format(str);
        String token = re.getString("access_token");
        int expires = re.getInt("expires_in") - 200;// 微信默认超时为7200秒，此处设置稍微短一点
        accessTokenStore.save(token, expires, System.currentTimeMillis());
    }
}
