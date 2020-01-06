package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxResContent;
import org.apache.http.HttpException;

/**
 * @author sterling
 * @email linktoyl@163.com
 * @date 2020/1/6 17:42
 */
public interface WxMiniApi {
    WxResContent getWxAcodeUnlimit(String appid, String scene, String path, int width) throws HttpException;
}
