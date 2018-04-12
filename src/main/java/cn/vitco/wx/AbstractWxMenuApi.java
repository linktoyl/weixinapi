package cn.vitco.wx;

import cn.vitco.wx.api.setting.WxMenuApi;
import cn.vitco.wx.config.WX_API_URL;
import cn.vitco.wx.entity.WxMenu;
import cn.vitco.wx.entity.WxResContent;
import cn.vitco.wx.exception.WxException;
import cn.vitco.wx.http.WxRequest;
import cn.vitco.wx.http.WxResponse;
import cn.vitco.wx.util.WxMap;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import java.util.List;

public abstract class AbstractWxMenuApi implements WxMenuApi {
    private final static Logger log = Logger.getLogger("WxApi_Log");

    @Override
    public WxResContent getCurrSelfMenu(String atoken) {
        if(atoken == null){
            throw new WxException("AccessToken参数为空!");
        }
        String url = String.format(WX_API_URL.WX_CURR_MENU_GET, atoken);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取当前自定义菜单异常:"+atoken, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }

    @Override
    public WxResContent getMenu(String atoken) {
        if(atoken == null){
            throw new WxException("AccessToken参数为空!");
        }
        String url = String.format(WX_API_URL.WX_MENU_GET, atoken);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取自定义菜单异常:"+atoken, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));    }

    @Override
    public WxResContent deleteMenu(String atoken) {
        if(atoken == null){
            throw new WxException("AccessToken参数为空!");
        }
        String url = String.format(WX_API_URL.WX_MENU_DEL, atoken);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.GET);
        WxResponse resp = null;
        try {
            resp = req.send();
        } catch (HttpException e) {
            log.error("删除自定义菜单异常:"+atoken, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));    }

    @Override
    public WxResContent createMenu(String atoken, List<WxMenu> wxmenus) {
        if(atoken == null){
            throw new WxException("AccessToken参数为空!");
        }
        if(wxmenus==null || wxmenus.size()<1 || wxmenus.size()>3){
            throw new WxException("自定义一级菜单数量错误!");
        }
        for (WxMenu menu: wxmenus) {
            if(menu.getSub_button() != null && (menu.getSub_button().size()<1 || menu.getSub_button().size()>5))
                throw new WxException("自定义二级菜单数量错误!");
        }

        String url = String.format(WX_API_URL.WX_MENU_CREATE, atoken);
        WxRequest req = new WxRequest(url, WxRequest.METHOD.POST);
        JSONObject para = new JSONObject();
        para.put("button", JSONArray.toJSON(wxmenus));
        WxResponse resp = null;
        try {
            req.setData(para.toString());
            resp = req.send();
        } catch (HttpException e) {
            log.error("获取当前自定义菜单异常:"+atoken, e.getCause());
        }
        if (resp == null || !resp.isOK()) {
            return null;
        }
        return WxResContent.format(resp.getContent("utf-8"));
    }
}
