package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxResContent;

/**
 * 微信用户相关接口
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:32
 *
 */
public interface WxUserApi {
    WxResContent user_info(String openid);

    WxResContent wx_mini_login(String code);

}
