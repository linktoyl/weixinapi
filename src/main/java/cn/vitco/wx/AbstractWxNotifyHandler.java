package cn.vitco.wx;

import cn.vitco.common.Lang;
import cn.vitco.common.Xmls;
import cn.vitco.wx.api.WxNotifyApi;
import cn.vitco.wx.entity.PayNotifyObject;
import cn.vitco.wx.exception.WxException;
import org.dom4j.DocumentException;

import java.util.Map;

/**
 * @author sterling
 * @email linktoyl@163.com
 * @date 2020/1/3 0:13
 */

public abstract class AbstractWxNotifyHandler implements WxNotifyApi {
    @Override
    public PayNotifyObject parseNotifyXml(String respXml) throws WxException {
        try {
            Map<String, Object> map = Xmls.xmlToMap(respXml, "xml");
            Object obj = Lang.mapToObject(map, PayNotifyObject.class);
            return (PayNotifyObject) obj;
        } catch (DocumentException e) {
            throw new WxException("微信通知返回报文解析失败");
        } catch (Exception e) {
            throw new WxException("微信通知解析Object失败");
        }
    }
}
