package cn.vitco.wx;

import cn.vitco.cache.redis.JedisClusterFactory;
import cn.vitco.common.Lang;
import cn.vitco.common.Xmls;
import cn.vitco.wx.aes.WxBizDataCrypt;
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
import cn.vitco.wx.exception.WxRunException;
import cn.vitco.wx.http.WxRequest;
import cn.vitco.wx.http.WxRequest.METHOD;
import cn.vitco.wx.http.WxResponse;
import cn.vitco.wx.util.WxMap;
import cn.vitco.wx.util.WxPaySSL;
import cn.vitco.wx.util.WxPaySign;
import net.sf.json.JSONObject;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import java.util.Set;
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
    public WxResContent user_info(String appid, String openid) {
        WxResContent wxres = user_info_safe(appid, openid);
        if(wxres.errcode()==40001){
            synchronized (lock) {
                try {
                    reflushAccessToken(appid);
                } catch (IllegalArgumentException e) {
                    log.error("刷新JsapiTicket出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新JsapiTicket出错[网络问题]:"+e.getMessage());
                }
            }
            wxres = user_info_safe(appid, openid);
        }
        return wxres;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * @param access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openid
     * @return
     */
    @Override
    public WxResContent user_oauth(String access_token, String openid) {
        WxRequest req = new WxRequest(WX_API_URL.WX_OAUTH_USERINFO, METHOD.GET);
        WxMap params = new WxMap();
        params.put("access_token", access_token);
        params.put("openid", openid);
        params.put("lang", "zh_CN");
        req.setParams(params);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("拉取用户信息(需scope为 snsapi_userinfo)异常:"+openid, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    private WxResContent user_info_safe(String appid, String openid){
        WxRequest req = new WxRequest(WX_API_URL.WX_GET_USERINFO, METHOD.GET);
        WxMap params = new WxMap();
        params.put("access_token", getAccessToken(appid));
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
     *  微信小程序
     * 登录凭证 code 获取 session_key 和 openid
     * @param code
     * @return
     */
    public WxResContent wx_mini_login(String code){
        String url = String.format(WX_API_URL.WX_MINI_LOGIN, WX_API_CONFIG.getMini_appid(), WX_API_CONFIG.getMini_appsecret(), code);
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

    /**
     * 解析微信小程序数据
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     * @throws WxException
     */
    public WxMap wx_mini_data_decrypt(String sessionKey, String encryptedData, String iv) throws WxException{
        WxBizDataCrypt dataCrypt = new WxBizDataCrypt(sessionKey);
        String result = dataCrypt.decryptData(encryptedData, iv);
        WxMap wxMap = new WxMap();
        JSONObject json = JSONObject.fromObject(result);
        if(json != null){
            JSONObject watermark = (JSONObject) json.remove("watermark");
            String appid = watermark.getString("appid");
            if(!appid.equals(WX_API_CONFIG.getMini_appid()))
                throw new WxException("解密数据与设置的AppID匹配失败!");
            Set<Map.Entry> ens = json.entrySet();
            for (Map.Entry en: ens) {
                wxMap.put(en.getKey().toString(), en.getValue().toString());
            }
            return wxMap;
        }
        throw new WxException("微信小程序数据解密失败!");
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
    public WxMap genJsSDKConfig(String appid, String url) throws WxException{
        String jt = this.getJsapiTicket(appid);
        if(jt == null)
            throw new WxException("生成JsSdkConfig失败, JsapiTicket为null!");
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = UUID.randomUUID().toString();

        String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s", jt, nonceStr, timestamp, url);
        String signature = Lang.sha1(str);

        WxMap map = new WxMap();
        map.put("url", url);
        map.put("jsapi_ticket", jt);
        map.put("appid", appid);
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
            throw new WxRunException("支付结果异常!", e);
        }
    }

    /**
     * 微信支付公共POST方法（不带证书）
     *
     * @param url      请求路径
     * @param key      商户KEY
     * @param params   参数
     * @return
     */
    public WxMap postPay(String url, String key, Map<String, Object> params) throws WxException{
        params.remove("sign");
        String sign = WxPaySign.createSign(key, params);
        params.put("sign", sign);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.POST);
        String reqdata = Xmls.mapToXml(params, true);
        log.info("微信支付XMl:\n %s" + reqdata);
        req.setData(reqdata);
        try {
            WxResponse resp = req.send();
            if (!resp.isOK())
                throw new IllegalStateException("postPay without SSL, resp code=" + resp.getStatus());
            String respContent = resp.getContent("UTF-8");
            log.info("微信支付返回:\n " + respContent);
            return new WxMap(Xmls.xmlToMap(respContent,"xml"));
        } catch (Exception e) {
            throw new WxRunException("支付结果异常!", e);
        }
    }


    @Override
    public String getJsapiTicket(String appid) {
        WxJsapiTicket jt = jsapiTicketStore.get(appid);
        if (jt == null) {
            synchronized (lock) {
                try {
                    reflushJsapiTicket(appid);
                } catch (IllegalArgumentException e) {
                    log.error("刷新JsapiTicket出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新JsapiTicket出错[网络问题]:"+e.getMessage());
                }
                jt = jsapiTicketStore.get(appid);
            }
        }
        if(jt == null){
            throw new WxException("获取JsApiTicket失败!");
        }
        return jt.getTicket();
    }

    @Override
    public String getAccessToken(String appid) {
        WxAccessToken at = accessTokenStore.get(appid);
        if (at == null) {
            synchronized (lock) {
                try {
                    reflushAccessToken(appid);
                } catch (IllegalArgumentException e) {
                    log.error("刷新AccessToken出错:"+e.getMessage());
                } catch (HttpException e) {
                    log.error("刷新AccessToken出错[网络问题]:"+e.getMessage());
                }
                at = accessTokenStore.get(appid);
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
    protected synchronized void reflushJsapiTicket(String appid) throws IllegalArgumentException, HttpException {
        String accessToken = this.getAccessToken(appid);

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
        jsapiTicketStore.save(appid, ticket, expires, System.currentTimeMillis());
    }

    /**
     * 刷新AccessToken
     * @author Sterling 【linktoyl@163.com】
     * @date 2018/3/15 0:48
     *
     */
    protected synchronized void reflushAccessToken(String appid) throws IllegalArgumentException, HttpException{
        String url = String.format(WX_API_URL.WX_GET_ACCESSTOKEN, appid, WX_API_CONFIG.getAppsecretByAppid(appid));
        if (log.isDebugEnabled())
            log.debug("刷新access_token 地址:" + url);

        WxResponse resp = new WxRequest(url, METHOD.GET).send();

        if (resp == null || !resp.isOK())
            throw new IllegalArgumentException("刷新AccessToken失败!");
        String str = resp.getContent();
        WxResContent re = WxResContent.format(str);
        String token = re.getString("access_token");
        if (log.isDebugEnabled())
            log.debug("刷新access_token 返回: " + str);
        if(token==null || token.isEmpty()) {
            log.error("刷新access_token 错误: " + str);
            throw new IllegalArgumentException("刷新AccessToken失败!");
        }

        int expires = re.getInt("expires_in") - 200;// 微信默认超时为7200秒，此处设置稍微短一点
        accessTokenStore.save(appid, token, expires, System.currentTimeMillis());
    }

    @Override
    public WxResContent setIndustry(String appid, String industry1, String industry2) {
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_SET_INDUSTRY, accessToken);
        WxRequest req = new WxRequest(url, METHOD.POST);
        WxMap params = new WxMap();
        params.put("industry_id1", industry1);
        params.put("industry_id2", industry2);
        try {
            log.info(params.toString());
            req.setData("{'industry_id1'='21', 'industry_id2'='2'}".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WxResponse resp = null;
        try {
            resp = req.send();
            log.info(resp.getContent());
        } catch (HttpException e) {
            log.error("设置账号所属行业信息异常", e.getCause());
        }
        return null;
    }

    @Override
    public WxResContent getIndustry(String appid) {
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_GET_INDUSTRY, accessToken);
        WxRequest req = new WxRequest(url, METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取帐号设置的行业信息异常", e.getCause());
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    @Override
    public WxResContent addTemplateShortId(String appid, String template_id_short) {
        if(template_id_short == null || template_id_short.isEmpty())
            return null;
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_ADD_SHORT_ID, accessToken);
        WxRequest req = new WxRequest(url, METHOD.POST);

        WxResponse resp = null;
        try {
            JSONObject param = new JSONObject();
            param.put("template_id_short", template_id_short);
            req.setData(param.toString().getBytes("utf-8"));
            resp = req.send();
            log.info(resp.getContent());
        } catch (HttpException e) {
            log.error("从行业模板库选择模板到帐号后台异常", e.getCause());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    @Override
    public WxResContent getTemplateList(String appid) {
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_ALL_LIST, accessToken);
        WxRequest req = new WxRequest(url, METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取模板列表信息异常", e.getCause());
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    @Override
    public WxResContent delTemplate(String appid, String template_id) {
        if(template_id == null || template_id.isEmpty())
            return null;
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_DEL, accessToken);
        WxRequest req = new WxRequest(url, METHOD.POST);

        WxResponse resp = null;
        try {
            JSONObject param = new JSONObject();
            param.put("template_id", template_id);
            req.setData(param.toString().getBytes("utf-8"));
            resp = req.send();
            log.info(resp.getContent());
        } catch (HttpException e) {
            log.error("删除模板信息异常", e.getCause());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    @Override
    public WxResContent sendTemplateMsg(String appid, TemplateMsgData tempData) {
        if(tempData == null)
            return null;
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_TEMPLATE_SEND, accessToken);
        WxRequest req = new WxRequest(url, METHOD.POST);

        WxResponse resp = null;
        try {
            log.info(tempData.toString());
            req.setData(tempData.toString().getBytes("utf-8"));
            resp = req.send();
            log.info(resp.getContent());
        } catch (HttpException e) {
            log.error("设置账号所属行业信息异常", e.getCause());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }


    @Override
    public WxMap pay_unifiedorder(String key, WxPayOrder order) throws WxException {
        Map<String, Object> params = Lang.objectToMap(order);
        return this.postPay(WX_API_URL.WX_PAY_ORDER, key, params);
    }

    @Override
    public WxMap query_unifiedorder(String key, Map<String, Object> params) throws WxException {
        return this.postPay(WX_API_URL.WX_QUERY_ORDER, key, params);
    }

    @Override
    public WxMap close_unifiedorder(String key, Map<String, Object> params) throws WxException {
        return this.postPay(WX_API_URL.WX_CLOSE_ORDER, key, params);
    }

    @Override
    public WxMap pay_refund(String key, WxPayRefund refund, String certfile, String password) throws WxException {
        Map<String, Object> params = Lang.objectToMap(refund);
        return this.postPay(WX_API_URL.WX_REFUND, key, params, certfile, password);
    }

    @Override
    public WxMap query_refund(String key, Map<String, Object> params) throws WxException {
        return this.postPay(WX_API_URL.WX_QUERY_REFUND, key, params);
    }

    @Override
    public WxResContent getWxAcodeUnlimit(String appid, String scene, String page, int width) throws HttpException {
        String accessToken = getAccessToken(appid);
        String url = String.format(WX_API_URL.WX_MINI_WXACODE_UNLIMIT, accessToken);
        WxRequest req = new WxRequest(url, METHOD.POST);
        WxMap params = new WxMap();
        params.put("scene", scene);
        params.put("page", page);
        params.put("width", width);
        try {
            log.info(params.toString());
            req.setData(params.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WxResponse resp = req.send();
        log.info(resp.getContent());
        return WxResContent.format(resp.getContent());
    }
}
