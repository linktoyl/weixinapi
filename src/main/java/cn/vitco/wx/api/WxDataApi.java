package cn.vitco.wx.api;

import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;

/**
 * 微信数据解密 接口
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:31
 *
 */
public interface WxDataApi {
    WxMap wx_mini_data_decrypt(String sessionKey, String encryptedData, String iv) throws WxException;

}
