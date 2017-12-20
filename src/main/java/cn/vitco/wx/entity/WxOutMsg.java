package cn.vitco.wx.entity;

import cn.vitco.wx.util.WxMap;

/**
 * Created by Sterling on 2017/12/20.
 */
public class WxOutMsg extends WxMap {
    /**
     *
     */
    private static final long serialVersionUID = 2550035913250961292L;
    protected transient String _raw;

    public WxOutMsg() {}

    public WxOutMsg(String msgType) {
        setMsgType(msgType);
    }

    public WxOutMsg setFromUserName(String fromUserName){
        put("FromUserName", fromUserName);
        return this;
    }

    public String getFromUserName(){
        return (String)get("FromUserName");
    }

    public WxOutMsg setToUserName(String ToUserName){
        put("ToUserName", ToUserName);
        return this;
    }

    public String getToUserName(){
        return (String)get("ToUserName");
    }

    public WxOutMsg setMsgType(String msgType){
        put("MsgType", msgType);
        return this;
    }

    public String getMsgType(){
        return (String)get("MsgType");
    }

    public WxOutMsg setContent(String content){
        put("Content", content);
        return this;
    }

    public String getContent(){
        return (String)get("Content");
    }

    public WxOutMsg setCreateTime(long createTime){
        put("CreateTime", createTime);
        return this;
    }

    public long getCreateTime(){
        return getLong("CreateTime", 0);
    }

    public WxOutMsg setMedia_id(String media_id){
        put("Media_id", media_id);
        return this;
    }

    public String getMedia_id(){
        return (String)get("Media_id");
    }

    public String raw() {
        return _raw;
    }

    public void raw(String raw) {
        this._raw= raw;
    }
}

