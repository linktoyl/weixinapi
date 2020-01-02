package cn.vitco.wx.entity;

import cn.vitco.common.Lang;
import cn.vitco.common.Xmls;

/**
 * @author sterling
 * @email linktoyl@163.com
 * @date 2020/1/3 0:09
 */

public class WxNotifyResp {
    private String return_code;
    private String return_msg;

    public WxNotifyResp() {
        this.return_code = "SUCCESS";
        this.return_msg = "OK";
    }

    public WxNotifyResp(String code, String msg) {
        this.return_code = code;
        this.return_msg = msg;
    }

    public String toXml() {
        return Xmls.mapToXml(Lang.objectToMap(this), true);
    }
}
