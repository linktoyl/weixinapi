package cn.vitco.wx.api;

import cn.vitco.wx.entity.WxInMsg;
import cn.vitco.wx.entity.WxOutMsg;

/**
 * Created by Sterling on 2017/12/20.
 */
public interface WxHandlerApi {
    boolean check(String token, String signature, String timestamp, String nonce);

    WxOutMsg text(WxInMsg msg);
    WxOutMsg image(WxInMsg msg);
    WxOutMsg voice(WxInMsg msg);
    WxOutMsg video(WxInMsg msg);
    WxOutMsg location(WxInMsg msg);
    WxOutMsg link(WxInMsg msg);
    //WxOutMsg event(WxInMsg msg);
    WxOutMsg shortvideo(WxInMsg msg);

    WxOutMsg eventSubscribe(WxInMsg msg);
    WxOutMsg eventUnsubscribe(WxInMsg msg);
    WxOutMsg eventScan(WxInMsg msg);
    WxOutMsg eventLocation(WxInMsg msg);
    WxOutMsg eventClick(WxInMsg msg);
    WxOutMsg eventView(WxInMsg msg);
    WxOutMsg eventTemplateJobFinish(WxInMsg msg);
    WxOutMsg eventScancodePush(WxInMsg msg);
    WxOutMsg eventScancodeWaitMsg(WxInMsg msg);
    WxOutMsg eventScancodePicSysphoto(WxInMsg msg);
    WxOutMsg eventScancodePicPhotoOrAlbum(WxInMsg msg);
    WxOutMsg eventScancodePicWeixin(WxInMsg msg);
    WxOutMsg eventLocationSelect(WxInMsg msg);

    WxOutMsg defaultMsg(WxInMsg msg);

    WxOutMsg handle(WxInMsg in);
}
