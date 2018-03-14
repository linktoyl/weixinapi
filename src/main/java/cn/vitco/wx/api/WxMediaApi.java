package cn.vitco.wx.api;

import java.io.InputStream;

/**
 * 微信 媒体接口
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:31
 *
 */
public interface WxMediaApi {
    InputStream downMedia(String accessToken, String mediaId);
}
