package cn.vitco.wx.config;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Sterling on 2017/12/12.
 */
public class WX_API_CONFIG {

    private static String accessTokenCache;
    private static String token;
    private static String appid;
    private static String appsecret;
    private static String encodingAesKey;
    //private static int retryTimes;// 默认access_token时效时重试次数

    private static String payKey;
    private static String mchid;
    private static String certUrl;

    private static String mini_appid;
    private static String mini_appsecret;

    private static Map<String, String> appsecretMap = new HashMap<String, String>();

    private static String checkName;


    private final static String WX_CONFIG_FILENAME = "wx-config.properties";

    static{
        try {
            Properties prop = new Properties();
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/resource/"+WX_CONFIG_FILENAME);
            prop.load(is);
            token = prop.getProperty("WX_TOKEN");
            appid = prop.getProperty("APP_ID");
            appsecret = prop.getProperty("APP_SECRET");
            appsecretMap.put(appid, appsecret);
            encodingAesKey = prop.getProperty("ENCODING_AESKEY");
            //retryTimes = Integer.valueOf(prop.getProperty("RETRY_TIMES","3"));
            accessTokenCache = prop.getProperty("WX_ACCESSTOKEN_CACHE","Memory");

            payKey = prop.getProperty("PAY_KEY");
            mchid = prop.getProperty("MCHID");
            certUrl = prop.getProperty("CERT_PATH");

            mini_appid = prop.getProperty("MINI_APP_ID","");
            mini_appsecret = prop.getProperty("MINI_APP_SECRET","");
            appsecretMap.put(mini_appid, mini_appsecret);
            checkName = prop.getProperty("PAY_CHECK_NAME", "NO_CHECK");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAccessTokenCache() {
        return accessTokenCache;
    }

    public static String getToken() {
        return token;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getAppsecretByAppid(String appid) {
        return appsecretMap.get(appid);
    }

    public static String getAppsecret() {
        return appsecret;
    }

    public static String getEncodingAesKey() {
        return encodingAesKey;
    }

    public static String getPayKey() {
        return payKey;
    }

    public static String getMchid() {
        return mchid;
    }

    public static String getCertUrl() {
        return certUrl;
    }

    public static String getCheckName() {
        return checkName;
    }

    public static String getMini_appid() {
        return mini_appid;
    }


    public static String getMini_appsecret() {
        return mini_appsecret;
    }


}
