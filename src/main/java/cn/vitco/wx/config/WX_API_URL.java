package cn.vitco.wx.config;

/**
 * 微信API接口  URL地址
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:04
 *
 */
public class WX_API_URL {

    /**
     * 微信公众号 授权
     */
    public final static String WX_OAUTH_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public final static String WX_OAUTH_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 微信用户信息
     */
    public final static String WX_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    /**
     * 微信公众号 accesstoken 和 jsapiticket
     */
    public final static String WX_GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public final static String WX_GET_JSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    /**
     * 微信商户平台 企业付款接口
     */
    public final static String WX_PAY_TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    public final static String WX_QUERY_TRANSFERS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    /**
     * 微信商户平台 红包接口
     */
    public final static String WX_QUERY_REDPACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
    public final static String WX_PAY_REDPACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    /**
     * 微信商户平台 收款API
     */
    public final static String WX_PAY_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public final static String WX_QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";
    public final static String WX_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";
    public final static String WX_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public final static String WX_QUERY_REFUND = "https://api.mch.weixin.qq.com/pay/refundquery";
    public final static String WX_DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";

    /**
     * 微信公众号 媒体API
     */
    public final static String WX_DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";

    /**
     * 模板消息API URL
     */
    public final static String WX_TEMPLATE_SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=%s";
    public final static String WX_TEMPLATE_GET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=%s";
    public final static String WX_TEMPLATE_ADD_SHORT_ID = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=%s";
    public final static String WX_TEMPLATE_ALL_LIST = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
    public final static String WX_TEMPLATE_DEL = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=%s";
    public final static String WX_TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    /**
     * 微信公众号 自定义菜单API  URL
     *
     */
    public final static String WX_MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    public final static String WX_MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
    public final static String WX_MENU_DEL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";
    public final static String WX_CURR_MENU_GET = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=%s";


    /**
     * 微信小程序 登录
     */
    public final static String WX_MINI_LOGIN = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

}
