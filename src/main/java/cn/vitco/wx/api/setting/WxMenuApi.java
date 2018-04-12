package cn.vitco.wx.api.setting;


import cn.vitco.wx.entity.WxMenu;
import cn.vitco.wx.entity.WxResContent;

import java.util.List;

/**
 * 微信公众号 自定义菜单 相关接口API
 */
public interface WxMenuApi {
    /**
     * 创建自定义菜单
     * @param atoken   待创建菜单的微信公众号的accesstoken
     * @param wxmenus   一级菜单列表个数1-3
     * @return
     */
    WxResContent createMenu(String atoken, List<WxMenu> wxmenus);

    /**
     * 获取自定义菜单 （暂时不处理返回结果中的个性化菜单）
     * @param atoken 待获取菜单的微信公众号accesstoken
     * @return
     */
    WxResContent getMenu(String atoken);

    /**
     * 删除自定义菜单
     * 调用此接口会删除默认菜单及全部个性化菜单
     * @param atoken 待删除菜单的微信公众号accesstoken
     * @return
     */
    WxResContent deleteMenu(String atoken);

    /**
     * 本接口将会提供公众号当前使用的自定义菜单的配置
     * 如果公众号是通过API调用设置的菜单，则返回菜单的开发配置;
     * 而如果公众号是在公众平台官网通过网站功能发布菜单，
     * 则本接口返回运营者设置的菜单配置
     * @param atoken 待获取菜单的微信公众号accesstoken
     * @return
     */
    WxResContent getCurrSelfMenu(String atoken);
}
