package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxPayOrder;
import cn.vitco.wx.entity.WxPayRefund;
import cn.vitco.wx.entity.WxPayment;
import cn.vitco.wx.entity.WxRedPack;
import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;

import java.io.File;
import java.util.Map;

/**
 * 微信支付相关接口
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:32
 *
 */
public interface WxPayApi {
    WxMap send_redpack(String key, WxRedPack wxRedPack, String certfile, String password) throws WxException;
    WxMap query_redpackRecode(String key, Map<String, Object> params, String certfile, String password)  throws WxException;
    WxMap pay_transfers(String key, WxPayment wxPayment, String certfile, String password) throws WxException;
    WxMap query_transfers(String key, Map<String, Object> params, String certfile, String password)  throws WxException;

    WxMap pay_unifiedorder(String key, WxPayOrder order) throws WxException;
    WxMap query_unifiedorder(String key, Map<String, Object> params) throws WxException;
    WxMap close_unifiedorder(String key, Map<String, Object> params) throws WxException;
    WxMap pay_refund(String key, WxPayRefund refund, String certfile, String password) throws WxException;
    WxMap query_refund(String key, Map<String, Object> params) throws WxException;



}
