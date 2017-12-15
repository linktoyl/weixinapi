package cn.vitco.wx.entity;

/**
 * 微信企业支付接口订单实体类
 *
 * Created by Sterling on 2017/12/12.
 */
public class WxPayment {
    //微信分配的账号ID（企业号corpid即为此appId）
    private String mch_appid;
    //微信支付分配的商户号
    private String mchid;
    //随机字符串，不长于32位
    private String nonce_str;
    //商户订单号，需保持唯一性(只能是字母或者数字，不能包含有符号)
    private String partner_trade_no;
    //商户appid下，某用户的openid
    private String openid;
    //NO_CHECK：不校验真实姓名 ;   FORCE_CHECK：强校验真实姓名
    private String check_name;
    //收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
    private String re_user_name;
    //企业付款金额，单位为分
    private String amount;
    //企业付款操作说明信息。必填。
    private String desc;
    //调用接口的机器Ip地址
    private String spbill_create_ip;
    private String sign;

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getRe_user_name() {
        return re_user_name;
    }

    public void setRe_user_name(String re_user_name) {
        this.re_user_name = re_user_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
