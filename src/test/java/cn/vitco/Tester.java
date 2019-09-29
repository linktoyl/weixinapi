package cn.vitco;

import cn.vitco.cache.redis.JedisClusterFactory;
import cn.vitco.common.HttpUtils;
import cn.vitco.security.aes.AESSecurityUtil;
import cn.vitco.wx.AbstractWxApi;
import cn.vitco.wx.VitcoWxApi;
import cn.vitco.wx.WxMenuApi;
import cn.vitco.wx.config.WX_API_CONFIG;
import cn.vitco.wx.entity.*;
import cn.vitco.wx.entity.menu.MenuType;
import cn.vitco.wx.http.WxRequest;
import cn.vitco.wx.util.WxMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpException;
import redis.clients.jedis.JedisCluster;
import sun.plugin.javascript.navig.LinkArray;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sterling on 2017/12/12.
 */
public class Tester {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws Exception {
        changOpenid();



    }

    public static void changOpenid() throws IOException, HttpException {
        String url = "http://api.weixin.qq.com/cgi-bin/changeopenid?access_token=";
        url = url + hqtk();
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("from_appid", "wxfdefd866398c3d98");
        List<String> openid_list = new ArrayList<>();
        openid_list.add("oGZSJ0Uy-lDW_YDtlLov41FwUlis");
        dataMap.put("openid_list", openid_list);
        HashMap<String, String> reqHeaders = new HashMap<String, String>();
        //WxRequest wxRequest = new WxRequest(url, WxRequest.METHOD.POST);
        String dataStr = JSON.toJSONString(dataMap);

        //wxRequest.setData(dataStr.getBytes("utf-8"));

        //System.out.println(wxRequest.send().getContent());

        System.out.println(HttpUtils.sendMsg(url, dataMap, reqHeaders));


    }

    public static void test_query_hb() {
        //VitcoWxApi wxApi = new VitcoWxApi();
        //wxApi.query_redpackRecode()

    }

    public static String hqtk(){
        Long ks=System.currentTimeMillis();
        try{
            HashMap<String, String> reqHeaders = new HashMap<String, String>();
            HashMap<String, Object> reqParams = new HashMap<String, Object>();
            Map<String, Object> zbjMap = new HashMap<String, Object>();
            Map<String, Object> zbjDataMap = new HashMap<String, Object>();
//		        "tran_id": "CQSW.DZSWJ.CX.ACCESSTOKEN",
//		        "channel_id": "CQSW.DZSWJ.YJ",
//		        "tran_seq": "d857b72a849449399c00b217db42364c1",
//		        "tran_time": "2019-01-02 10:00:00",
//		        "data": {}
            zbjDataMap.put("tran_id", "CQSW.DZSWJ.CX.ACCESSTOKEN");
            zbjDataMap.put("channel_id", "CQSW.DZSWJ.YJ");
            zbjDataMap.put("tran_seq", UUID.randomUUID().toString().replaceAll("-", ""));
            zbjDataMap.put("tran_time", sdf.format(new Date()));
            String zbjParams = JSON.toJSONString(zbjDataMap);
            // 加密
            String aeskey = "11bb077cfee54958a9f369ae835be162";
            String params = AESSecurityUtil.encryptAES(zbjParams, aeskey);
		       /* params="ev8blAq2YT7KARRQ6Kxhk94sjOdq3J3BCVz/5sxnJ0wS7wIrVlVxb/x18xQyOuCghlpkW/fksB5kHFAkbfbk8A==";
				reqParams.put("interfaceKey", "VITCOLOGINGETTOKEN");// CQSW.DZSWJ.SFRZ.ZRRLOGIN CQSW.DZSWJ.YHY
				reqParams.put("appId", "sy");
				reqParams.put("condition", params);*/
            String url="http://47.94.7.80:8080/dzswj_h5_api/dzswjServer";
            url="https://wbjr.chongqing.chinatax.gov.cn/ThirdJrServlet";
            String ret= HttpUtils.post_String(url, params, reqHeaders);
            JSONObject res = JSON.parseObject(ret);
            System.out.println("返回==="+res.getString("value"));
				/*String fhz=AESSecurityUtil.decryptAES(ret, aeskey);
				System.out.println(fhz);*/
            Long js=System.currentTimeMillis()-ks;
            System.out.println("耗时：----"+js);
            return res.getString("value");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void menu_test() {
        VitcoWxApi wxApi = new VitcoWxApi();
        WxMenuApi menuApi = new WxMenuApi();
        //String token = wxApi.getAccessToken();
        String token = "8_fbSi6Rg4TgWsTDy24-0e70IXUuCfyqjhimJ27AMVJUaBQOZx1rLQADROzW8U1-g0dWqFPtWBKGA4fHYfmH-YquvibS-BgJxTegvXuCm8iP7raQmuvwwDlKwmGhQ14d9VwDBOD-0eC0-hRwgfASVcAFAKCF";
        System.out.println("AccessToken："+token);

        /*WxResContent res = menuApi.getCurrSelfMenu(wxApi.getAccessToken());
        if(res.ok()) {
            System.out.println(res);
        }else {
            System.out.println(res.errcode()+res.errmsg());
        }*/

        //一级菜单
        List<WxMenu> list = new ArrayList<WxMenu>();
        List<WxMenu> list2 = new ArrayList<WxMenu>();
        WxMenu root = new WxMenu();
        root.setName("一机组");
        WxMenu menu = new WxMenu();
        menu.setType(MenuType.click);
        menu.setName("测试");
        menu.setKey("V1001_GOOD");
        list.add(root);
        list2.add(menu);
        root.setSub_button(list2);
        System.out.println(JSONArray.toJSON(list));
        System.out.println(menuApi.getMenu(token));
        //System.out.println(menuApi.deleteMenu(token));
        WxResContent res = menuApi.createMenu(token, list);
        if(res.ok()) {
            System.out.println(res);
        }else {
            System.out.println(res.errcode()+res.errmsg());
        }
    }

    public static void log(String str){
        System.out.println(str);
    }

    public static void testTemplate() {
        VitcoWxApi wxapi = new VitcoWxApi();

        TemplateMsgData template = new TemplateMsgData();
        template.setTouser("oXuE2450eqGczCElozK9LNFfSR6c");
        /*template.setTemplate_id("iPItszYei9mEAYWwnjOJZLRaZCMu-uXpoTtjBjpl8yM");
        TemplateMsgData.DataLabel first = template.new DataLabel("您6月5日开具的发票，中了2018年3月1日重庆市国税局第1期二次摇奖的的三等奖");
        TemplateMsgData.DataLabel remark = template.new DataLabel("请于60日内到市国家税务局指定的X地领取奖金，逾期未兑视为放弃。详情请咨询纳税服务热线12366。");
        TemplateMsgData.Data data = template.new Data(first, remark);
        TemplateMsgData.DataLabel keynote1 = template.new DataLabel("\n150000000000");
        TemplateMsgData.DataLabel keynote2 = template.new DataLabel("00000001");
        TemplateMsgData.DataLabel keynote3 = template.new DataLabel("三等奖");
        TemplateMsgData.DataLabel keynote4 = template.new DataLabel("2万元");*/
        template.setTemplate_id("iIpeMgAVPaI8kMpwpvXhvziRx5p88yvNGGRPwzl7NDM");
        TemplateMsgData.DataLabel first = template.new DataLabel("您6月5日开具的发票，中了2018年3月1日重庆市国税局第1期二次摇奖的的三等奖");
        TemplateMsgData.DataLabel remark = template.new DataLabel("请于60日内到市国家税务局指定的X地领取奖金，逾期未兑视为放弃。详情请咨询纳税服务热线12366。");
        TemplateMsgData.Data data = template.new Data(first, remark);
        TemplateMsgData.DataLabel keynote1 = template.new DataLabel("\n150000000000");
        TemplateMsgData.DataLabel keynote2 = template.new DataLabel("00000001");
        TemplateMsgData.DataLabel keynote3 = template.new DataLabel("三等奖");
        TemplateMsgData.DataLabel keynote4 = template.new DataLabel("2万元");
        data.setKeynotes(keynote1,keynote2,keynote3,keynote4);
        template.setData(data);

        log(template.toString());

        WxResContent res1 = wxapi.sendTemplateMsg("wx4cd5a3c1a8e22248",template);

        log(res1.toString());
        WxResContent res = wxapi.getTemplateList("wx4cd5a3c1a8e22248");

        log(res.toString());
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

    public static void test(){
        //JedisClusterFactory fac = new JedisClusterFactory();
        //fac.getJedisCluster().del("accessToken@accessToken");
        VitcoWxApi wxapi = new VitcoWxApi();
        /*WxResContent wxat =  wxapi.user_info("o3XrQw6NdLPA821Nt7_lnQgv9SP8");
        System.out.println(JSONObject.toJSON(wxat));
        WxMap wxat1 =  wxapi.genJsSDKConfig("fdsfsd");
        System.out.println(JSONObject.toJSON(wxat1));*/
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
        wxapi.pay_transfers(WX_API_CONFIG.getPayKey(), genWxPayment(), WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());
        //1450326002201712121706369279
        /*Map<String, Object> map = new HashMap<String, Object>();
        String noncestr = getRandomStringByLength(20);// 随机字符串
        map.put("nonce_str", noncestr);
        map.put("partner_trade_no", "1450326002201712121706369279");
        map.put("appid", WX_API_CONFIG.getAppid());
        map.put("mch_id", WX_API_CONFIG.getMchid());
        wxApi.query_transfers(WX_API_CONFIG.getPayKey(), map, WX_API_CONFIG.getCertUrl(), WX_API_CONFIG.getMchid());*/

    }

    /**
     * 读取CSV文件
     * @param filePath 文件路径（包括文件名）
     * @param hasTitle 是否有标题行
     * @return
     */
    public static List<List<String>> readCSV(String filePath, boolean hasTitle){
        List<List<String>> data=new ArrayList<>();
        String line=null;
        try {
            //BufferedReader bufferedReader=new BufferedReader(new FileReader(filePath));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"utf-8"));
            if (hasTitle){
                //第一行信息，为标题信息
                line = bufferedReader.readLine();
                String[] items=line.split(",");
                data.add(Arrays.asList(items));
                System.out.println("标题行："+line);
            }

            int i=0;
            while((line=bufferedReader.readLine())!=null){
                i++;
                //数据行
                String[] items=line.split(",");
                data.add(Arrays.asList(items));
                System.out.println("第"+i+"行："+line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 写CSV文件
     * @param data 数据
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param hasTitle 是否有标题行
     */
    public static void  writeCSV(List<List<String>> data, String filePath,String fileName,boolean hasTitle){
        File csvFile=new File(filePath+"/"+fileName);
        BufferedWriter bufferedWriter=null;
        String line=null;
        try {
            // bufferedWriter=new BufferedWriter(new FileWriter(csvFile,true));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),"utf-8"));
            for (int i=0;i<data.size();i++){
                line=null;
                if (hasTitle){
                    line=getLine(data.get(i));
                }
                System.out.println(line.toString());
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取一行数据的csv格式的字符串
     * @param items
     * @return
     */
    private static String getLine(List<String> items){
        StringBuilder line=new StringBuilder();
        for (int i=0;i<items.size();i++){
            line.append(items.get(i));
            if (i<(items.size()-1)){
                line.append(",");
            }
        }
        return line.toString();
    }
}
