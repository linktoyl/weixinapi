package cn.vitco;

import cn.vitco.cache.redis.JedisClusterFactory;
import cn.vitco.wx.VitcoWxApi;
import cn.vitco.wx.config.WX_API_CONFIG;
import cn.vitco.wx.entity.WxAccessToken;
import cn.vitco.wx.entity.WxPayment;
import cn.vitco.wx.entity.WxRedPack;
import cn.vitco.wx.entity.WxResContent;
import cn.vitco.wx.util.WxMap;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sterling on 2017/12/12.
 */
public class Tester {

    public static void main(String[] args) {
        //JedisClusterFactory fac = new JedisClusterFactory();
        //fac.getJedisCluster().del("accessToken@accessToken");
        VitcoWxApi wxapi = new VitcoWxApi();
        WxResContent wxat =  wxapi.user_info("o3XrQw6NdLPA821Nt7_lnQgv9SP8");
        System.out.println(JSONObject.toJSON(wxat));
        WxMap wxat1 =  wxapi.genJsSDKConfig("fdsfsd");
        System.out.println(JSONObject.toJSON(wxat1));
       // VitcoWxApi wxApi = new VitcoWxApi();
        //wxApi.send_redpack(WX_API_CONFIG.getPayKey(), genWxRedPack(), WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());
        //--1450326002201712121625357215
       /* Map<String, Object> map = new HashMap<String, Object>();
        String noncestr = getRandomStringByLength(20);// 随机字符串
        map.put("nonce_str", noncestr);
        map.put("mch_billno", "1450326002201712121625357215");
        map.put("mch_id", WX_API_CONFIG.getMchid());
        map.put("appid", WX_API_CONFIG.getAppid());
        map.put("bill_type", "MCHT");
        wxApi.query_redpackRecode(WX_API_CONFIG.getPayKey(), map, WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());*/
        //wxApi.pay_transfers(WX_API_CONFIG.getPayKey(), genWxPayment(), WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());
        //1450326002201712121706369279
        /*Map<String, Object> map = new HashMap<String, Object>();
        String noncestr = getRandomStringByLength(20);// 随机字符串
        map.put("nonce_str", noncestr);
        map.put("partner_trade_no", "1450326002201712121706369279");
        map.put("appid", WX_API_CONFIG.getAppid());
        map.put("mch_id", WX_API_CONFIG.getMchid());
        wxApi.query_transfers(WX_API_CONFIG.getPayKey(), map, WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());*/
    }

    public static WxPayment genWxPayment(){
        String noncestr = getRandomStringByLength(20);// 随机字符串
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 设置日期格式
        String times = (df.format(new Date()) + ((int) (Math.random() * 10))).toString();// new
        String mchbillno = WX_API_CONFIG.getMchid() + times;// 商户订单号（每个订单号必须唯一）组成：
        System.out.println("流水号---" + mchbillno);
        WxPayment payment = new WxPayment();
        payment.setMch_appid(WX_API_CONFIG.getAppid());
        payment.setMchid(WX_API_CONFIG.getMchid());
        payment.setNonce_str(noncestr);
        payment.setPartner_trade_no(mchbillno);
        payment.setOpenid("o3XrQw6NdLPA821Nt7_lnQgv9SP8");
        payment.setCheck_name(WX_API_CONFIG.getCheckName());
        payment.setRe_user_name("杨林");
        payment.setAmount("2000");
        payment.setDesc("恭喜, 您参加重庆国税有奖发票系统, 获得一等奖!");
        payment.setSpbill_create_ip("192.168.13.86");

        return payment;
    }

    public static WxRedPack genWxRedPack(){
        String noncestr = getRandomStringByLength(20);// 随机字符串
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 设置日期格式
        String times = (df.format(new Date()) + ((int) (Math.random() * 10))).toString();// new
        String mchbillno = WX_API_CONFIG.getMchid() + times;// 商户订单号（每个订单号必须唯一）组成：
        // mch_id+yyyymmdd+10位一天内不能重复的数字（可以为hhMMssDD1）。接口根据商户订单号支持重入，
        // 如出现超时可再调用。
        // 1450326002201704151230441672
        //log.info("流水号---" + mchbillno);
        System.out.println("流水号---" + mchbillno);
        WxRedPack redpack = new WxRedPack();
        // String nickname="请自己填写";//提供方名称
        redpack.setSend_name("重庆国税有奖发票系统");// 红包发送者名称
        redpack.setNonce_str(noncestr);
        redpack.setMch_id(WX_API_CONFIG.getMchid());
        redpack.setMch_billno(mchbillno);
        //redpack.setScene_id(wxconfig.getScene_id());
        redpack.setRe_openid("o3XrQw6NdLPA821Nt7_lnQgv9SP8");// 用戶openID
        redpack.setTotal_amount((int) (2 * 100));// 付款金额
        redpack.setMin_value(100);// 最小红包金额
        redpack.setMax_value(800 * 100);// 最大红包金额
        redpack.setTotal_num(1);// 红包发放总人数
        redpack.setWishing("恭喜发票号码中奖！");// 红包祝福语
        // String clientip="192.168.12.63";//调用接口的机器Ip地址
        //String clientip;
        //try {
        //clientip = InetAddress.getLocalHost().getHostAddress().toString();
        //clientip = "218.70.65.77";
       // redpack.setClient_ip(clientip);
        //} catch (UnknownHostException e) {
        //	throw new VitcoException(ExceptionType.SYSTEM, e, "获取当前ip地址出错");
        //}
        redpack.setAct_name("重庆国税有奖发票");// 活动名称
        redpack.setRemark("恭喜中奖");
        redpack.setWxappid(WX_API_CONFIG.getAppid());// 公众账号appid
        return redpack;
    }

    /**
     * 生成指定长度的随机串
     *
     * @param length
     * @return
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
