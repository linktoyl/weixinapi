package cn.vitco.wx.entity;

import cn.vitco.wx.entity.menu.MenuType;

import java.util.List;

/**
 * Created by Sterling on 2017/12/20.
 */
public class WxMenu {

    private String name;
    private MenuType type;
    private String key;
    private String url;
    private String appid;
    private String pagepath;
    private String media_id;

    private List<WxMenu> sub_button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public List<WxMenu> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<WxMenu> sub_button) {
        this.sub_button = sub_button;
    }

    /*public String toString() {
        StringBuilder strBuilder = new StringBuilder();


        return strBuilder.toString();
    }*/
}

