package cn.vitco.wx.api;

import cn.vitco.wx.entity.TemplateMsgData;
import cn.vitco.wx.entity.WxResContent;
import cn.vitco.wx.util.WxMap;
/**
 * 微信公众号 模板消息接口
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:10
 *
 */
public interface WxTemplateMsg {
    WxResContent setIndustry(String appid, String industry1, String industry2);
    WxResContent getIndustry(String appid);
    WxResContent addTemplateShortId(String appid, String template_id_short);
    WxResContent getTemplateList(String appid);
    WxResContent delTemplate(String appid, String template_id);
    WxResContent sendTemplateMsg(String appid, TemplateMsgData tempData);
}
