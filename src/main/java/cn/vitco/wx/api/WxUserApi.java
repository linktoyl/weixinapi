package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxResContent;

/**
 * 微信用户相关接口
 *
 * Created by Sterling on 2017/12/11.
 */
public interface WxUserApi {
    WxResContent user_info(String openid);
}
