package cn.vitco.wx.config;

/**
 * Created by Sterling on 2017/12/11.
 */
public class WX_API_URL {

    public final static String WX_OAUTH_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public final static String WX_OAUTH_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    public final static String WX_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    public final static String WX_GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public final static String WX_GET_JSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    public final static String WX_PAY_TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    public final static String WX_QUERY_TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    public final static String WX_QUERY_REDPACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
    public final static String WX_PAY_REDPACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    public final static String WX_DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";

}
