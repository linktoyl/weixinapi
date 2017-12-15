package cn.vitco.wx.api;

import cn.vitco.wx.exception.WxException;

/**
 * Created by Sterling on 2017/12/11.
 */
public interface WxAccessTokenApi {
    String getAccessToken() throws WxException;
}
