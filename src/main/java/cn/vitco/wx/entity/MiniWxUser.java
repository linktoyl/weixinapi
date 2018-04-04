package cn.vitco.wx.entity;

import cn.vitco.wx.util.WxMap;

public class MiniWxUser {
    private String user_id;
    private String openid;
    private String unionid;
    private String wx_name;
    private String gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getWx_name() {
        return wx_name;
    }

    public void setWx_name(String wx_name) {
        this.wx_name = wx_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public static MiniWxUser format(WxMap wxMap){
        if(wxMap == null)
            return null;
        MiniWxUser wx_user = new MiniWxUser();
        wx_user.setAvatarUrl(wxMap.getString("avatarUrl",""));
        wx_user.setOpenid(wxMap.getString("openId", ""));
        wx_user.setWx_name(wxMap.getString("nickName", ""));
        wx_user.setUnionid(wxMap.getString("unionId", ""));
        String gender_temp = wxMap.getString("gender", "");
        String gender = "未知";
        if(gender_temp.equals("1"))
            gender = "男";
        else if(gender_temp.equals("2"))
            gender = "女";
        wx_user.setGender(gender);
        wx_user.setCity(wxMap.getString("city", ""));
        wx_user.setProvince(wxMap.getString("province", ""));
        wx_user.setCountry(wxMap.getString("country", ""));
        return wx_user;
    }
}
