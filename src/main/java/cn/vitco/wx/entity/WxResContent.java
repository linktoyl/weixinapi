package cn.vitco.wx.entity;

import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.util.WxMap;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * 微信接口答复信息 实体类
 *
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxResContent extends WxMap {
    /**
     *
     */
    private static final long serialVersionUID = 2288858107378340200L;

    public boolean ok() {
        return errcode() == 0;
    }

    public WxResContent check() {
        if (!ok())
            throw new WxException("errcode=" + errcode() + ", " + this);
        return this;
    }

    public int errcode() {
        return getInt("errcode", 0);
    }

    public String errmsg() {
        return getString("errmsg");
    }

    public String msg_id() {
        return getString("msg_id");
    }

    public String msg_status() {
        return getString("msg_status");
    }

    public String type() {
        return getString("type");
    }

    public int created_at() {
        return getInt("created_at", 0);
    }

    public Date created_at_date() {
        return new Date(created_at());
    }

    @SuppressWarnings("unchecked")
    public static WxResContent format(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            return null;
        }
        try {
            WxResContent resp = new WxResContent();
            JSONObject json = JSONObject.fromObject(jsonStr);
            Map<String, Object> tmpMap = (Map<String, Object>) JSONObject.toBean(json, Map.class);
            resp.putAll(tmpMap);
            return resp;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
