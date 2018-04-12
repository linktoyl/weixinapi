package cn.vitco.wx.entity.menu;


/**
 * 微信公众号 自定义菜单 按钮类型
 */
public enum MenuType {
    click("click"), view("view"), scancode_push("scancode_push"),
    scancode_waitmsg("scancode_waitmsg"), pic_sysphoto("pic_sysphoto"),
    pic_photo_or_album("pic_photo_or_album"), pic_weixin("pic_weixin"),
    location_select("location_select"), media_id("media_id"),
    view_limited("view_limited");

    private String name;
    private MenuType(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }


}
