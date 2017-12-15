package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxPayment;
import cn.vitco.wx.entity.WxRedPack;
import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;

import java.io.File;
import java.util.Map;

/**
 * 微信支付相关接口
 *
 * Created by Sterling on 2017/12/11.
 */
public interface WxPayApi {
    WxMap send_redpack(String key, WxRedPack wxRedPack, String certfile, String password) throws WxException;
    WxMap query_redpackRecode(String key, Map<String, Object> params, String certfile, String password)  throws WxException;
    WxMap pay_transfers(String key, WxPayment wxPayment, String certfile, String password) throws WxException;
    WxMap query_transfers(String key, Map<String, Object> params, String certfile, String password)  throws WxException;


}
