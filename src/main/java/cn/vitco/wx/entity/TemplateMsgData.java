package cn.vitco.wx.entity;

import net.sf.json.JSONObject;

import java.util.List;

/**
 * 模板消息 数据实体类
 * @author Sterling 【linktoyl@163.com】
 * @date 2018/3/14 21:14
 *
 */
public class TemplateMsgData {
    private String touser;
    private String template_id;
    private String url;
    private MiniProgram miniprogram;
    private Data data;

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMiniprogram(MiniProgram miniprogram) {
        this.miniprogram = miniprogram;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String toString(){
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", template_id);
        if(url != null) {
            json.put("url", url);
        }
        if(miniprogram != null) {
            json.put("miniprogram", miniprogram.toString());
        }
        json.put("data", data.toString());
        return json.toString();
    }

    /**
     * 模板消息数据中 小程序跳转设置
     */
    public class MiniProgram {
        private String appid;
        private String pagepath;

        public MiniProgram(String appid, String pagepath) {
            this.appid = appid;
            this.pagepath = pagepath;
        }

        public String getAppid() {
            return appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public String toString(){
            return "{\"appid\":\""+appid+"\",\"pagepath\":\""+pagepath+"\"}";
        }
    }

    /**
     * 模板消息数据中 关键字标签项
     */
    public class Data {
        private DataLabel first;
        private DataLabel[] keynotes;
        private DataLabel remark;

        public Data(DataLabel first, DataLabel remark, DataLabel... keynotes) {
            this.first = first;
            this.remark = remark;
            this.keynotes = keynotes;
        }

        public DataLabel getFirst() {
            return first;
        }

        public void setFirst(DataLabel first) {
            this.first = first;
        }

        public void setKeynotes(DataLabel... keynotes){
            this.keynotes = keynotes;
        }

        public String toString(){
            JSONObject json = new JSONObject();
            json.put("first", first.toString());
            if(keynotes != null){
                for (int i= 1; i<= keynotes.length; i++){
                    json.put("keyword"+i, keynotes[i-1].toString());
                }
            }
            json.put("remark", remark.toString());
            return json.toString();
        }
    }

    /**
     * 模板消息数据中 关键字标签项
     */
    public class DataLabel {
        private String value;
        private String color;

        public DataLabel(String value){
            this.value = value;
        }

        public DataLabel(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public String getValue() {
            return value;
        }

        public String getColor() {
            return color;
        }

        public String toString(){
            JSONObject json = new JSONObject();
            if(value != null && !value.isEmpty()){
                json.put("value", value);
            }
            if(color != null && !color.isEmpty()){
                json.put("color", color);
            }
            return json.toString();
        }
    }
}
