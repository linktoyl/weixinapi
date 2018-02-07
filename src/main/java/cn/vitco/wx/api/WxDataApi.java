package cn.vitco.wx.api;

import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;

/**
 * Created by Sterling on 2018/2/7.
 */
public interface WxDataApi {
    WxMap wx_mini_data_decrypt(String sessionKey, String encryptedData, String iv) throws WxException;

}
