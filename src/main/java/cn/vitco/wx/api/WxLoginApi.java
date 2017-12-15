package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxResContent;
import org.apache.http.HttpException;

/**
 * 微信登录相关接口
 *
 * Created by Sterling on 2017/12/11.
 */
public interface WxLoginApi {
    /**
     * 根据code换取access_token
     * @param auth_code
     * @return
     * @throws HttpException
     */
    WxResContent access_token(String auth_code) throws HttpException;

    /**
     * 刷新token
     * @param refresh_token
     * @return
     */
    WxResContent refresh_token(String refresh_token) throws HttpException;

    /**
     * 验证token是否还有效
     * @param token
     * @return
     */
    WxResContent auth(String token) throws HttpException;

    /**
     * 获取用户信息
     * @param openid
     * @param token
     * @return
     */
    WxResContent userinfo(String openid, String token) throws HttpException;
}
