package cn.vitco.wx.api;

import java.io.InputStream;

/**
 * Created by Sterling on 2017/12/11.
 */
public interface WxMediaApi {
    InputStream downMedia(String accessToken, String mediaId);
}
