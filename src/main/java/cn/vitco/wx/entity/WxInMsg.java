package cn.vitco.wx.entity;

import cn.vitco.wx.util.WxMap;

import java.util.Map;

/**
 * Created by Sterling on 2017/12/20.
 */
public class WxInMsg extends WxMap {

    /**
     *
     */
    private static final long serialVersionUID = -6918392404675879274L;
    protected transient String _raw;

    public WxInMsg(Map<String, Object> map){
        super(map);
    }

    public WxInMsg setFromUserName(String fromUserName){
        put("FromUserName", fromUserName);
        return this;
    }

    public String getFromUserName(){
        return (String)get("FromUserName");
    }

    public WxInMsg setToUserName(String toUserName){
        put("ToUserName", toUserName);
        return this;
    }

    public String getToUserName(){
        return (String)get("ToUserName");
    }

    public WxInMsg setEvent(String event){
        put("Event", event);
        return this;
    }

    public String getEvent(){
        return (String)get("Event");
    }

    public WxInMsg setEventKey(String eventKey){
        put("EventKey", eventKey);
        return this;
    }

    public String getEventKey(){
        return (String)get("EventKey");
    }

    public WxInMsg setMsgType(String msgType){
        put("MsgType", msgType);
        return this;
    }

    public String getMsgType(){
        return (String)get("MsgType");
    }

    public WxInMsg setContent(String content){
        put("Content", content);
        return this;
    }

    public String getContent(){
        return (String)get("Content");
    }

    public WxInMsg setCreateTime(long createTime){
        put("CreateTime", createTime);
        return this;
    }

    public long getCreateTime(){
        return getLong("CreateTime", 0);
    }

    public WxInMsg setMsgID(long msgID){
        put("MsgID", msgID);
        return this;
    }

    public long getMsgID(){
        return getLong("MsgID", 0);
    }

    public WxInMsg setPicUrl(String picUrl){
        put("PicUrl", picUrl);
        return this;
    }

    public String getPicUrl(){
        return (String)get("PicUrl");
    }

    public WxInMsg setMediaId(String mediaId){
        put("MediaId", mediaId);
        return this;
    }

    public String getMediaId(){
        return (String)get("MediaId");
    }

    public WxInMsg setFormat(String format){
        put("Format", format);
        return this;
    }

    public String getFormat(){
        return (String)get("Format");
    }

    public WxInMsg setThumbMediaId(String thumbMediaId){
        put("ThumbMediaId", thumbMediaId);
        return this;
    }

    public String getThumbMediaId(){
        return (String)get("ThumbMediaId");
    }

    public WxInMsg setRecognition(String recognition){
        put("Recognition", recognition);
        return this;
    }

    public String getRecognition(){
        return (String)get("Recognition");
    }

    public WxInMsg setLocation_X(double location_X){
        put("Location_X", location_X);
        return this;
    }

    public double getLocation_X(){
        return getDouble("Location_X", 0);
    }

    public WxInMsg setLocation_Y(double location_Y){
        put("Location_Y", location_Y);
        return this;
    }

    public double getLocation_Y(){
        return getDouble("location_Y", 0);
    }

    public WxInMsg setScale(double scale){
        put("Scale", scale);
        return this;
    }

    public double getScale(){
        return getDouble("Scale", 0);
    }

    public WxInMsg setLabel(String label){
        put("Label", label);
        return this;
    }

    public String getLabel(){
        return (String)get("Label");
    }

    public WxInMsg setTitle(String title){
        put("Title", title);
        return this;
    }

    public String getTitle(){
        return (String)get("Title");
    }

    public WxInMsg setDescription(String description){
        put("Description", description);
        return this;
    }

    public String getDescription(){
        return (String)get("Description");
    }

    public WxInMsg setUrl(String url){
        put("Url", url);
        return this;
    }

    public String getUrl(){
        return (String)get("Url");
    }

    public WxInMsg setStatus(String status){
        put("Status", status);
        return this;
    }

    public String getStatus(){
        return (String)get("Status");
    }

    public WxInMsg setExtkey(String extkey){
        put("Extkey", extkey);
        return this;
    }

    public String getExtkey(){
        return (String)get("Extkey");
    }

    public WxInMsg setTotalCount(int totalCount){
        put("TotalCount", totalCount);
        return this;
    }

    public int getTotalCount(){
        return getInt("TotalCount", 0);
    }

    public WxInMsg setFilterCount(int filterCount){
        put("FilterCount", filterCount);
        return this;
    }

    public int getFilterCount(){
        return getInt("FilterCount", 0);
    }

    public WxInMsg setSentCount(int sentCount){
        put("SentCount", sentCount);
        return this;
    }

    public int getSentCount(){
        return getInt("SentCount", 0);
    }

    public WxInMsg setErrorCount(int errorCount){
        put("ErrorCount", errorCount);
        return this;
    }

    public int getErrorCount(){
        return getInt("ErrorCount", 0);
    }

    public String raw() {
        return _raw;
    }

    public void raw(String raw) {
        this._raw= raw;
    }
}
