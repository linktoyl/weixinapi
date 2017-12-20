package cn.vitco.wx;

import cn.vitco.wx.api.WxHandlerApi;
import cn.vitco.wx.entity.WxInMsg;
import cn.vitco.wx.entity.WxOutMsg;
import cn.vitco.wx.util.WeiXinUtils;

/**
 * Created by Sterling on 2017/12/20.
 */
public abstract class AbstractWxHandler implements WxHandlerApi{
    public WxOutMsg text(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg image(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg voice(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg video(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg location(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg link(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventSubscribe(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventUnsubscribe(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventScan(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventLocation(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventClick(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventView(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventTemplateJobFinish(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg defaultMsg(WxInMsg msg) {
        if ("帮助".equals(msg.getContent()))
            return WeiXinUtils.respText(null, "支持的命令有: 你好 版本 帮助 appid 测试文本 测试新闻 回显");
        if ("你好".equals(msg.getContent()))
            return WeiXinUtils.respText(null, "你好!!");
        if ("版本".equals(msg.getContent()))
            return WeiXinUtils.respText(null, "Vitco 1.0.0 ");
        if ("appid".equals(msg.getContent()))
            return WeiXinUtils.respText(null, msg.getToUserName());
        if ("测试文本".equals(msg.getContent()))
            return WeiXinUtils.respText(null, "这真的是一条测试文本");

        return WeiXinUtils.respText("这是缺省回复哦.你发送的类型是:"+msg.getMsgType()+".");
    }

    public WxOutMsg handle(WxInMsg in) {
        return WeiXinUtils.handle(in, this);
    }

    public WxOutMsg eventScancodePush(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventScancodeWaitMsg(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventScancodePicSysphoto(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventScancodePicPhotoOrAlbum(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventScancodePicWeixin(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg eventLocationSelect(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public WxOutMsg shortvideo(WxInMsg msg) {
        return defaultMsg(msg);
    }

    public boolean check(String token, String signature, String timestamp, String nonce) {
        return WeiXinUtils.check(token, signature, timestamp, nonce);
    }
}
