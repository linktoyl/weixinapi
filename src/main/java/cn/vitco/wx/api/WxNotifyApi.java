package cn.vitco.wx.api;

import cn.vitco.wx.entity.PayNotifyObject;
import cn.vitco.wx.exception.WxException;

/**
 * @author sterling
 * @email linktoyl@163.com
 * @date 2020/1/2 23:54
 */

public interface WxNotifyApi {

    PayNotifyObject parseNotifyXml(String respXml) throws WxException;
}
