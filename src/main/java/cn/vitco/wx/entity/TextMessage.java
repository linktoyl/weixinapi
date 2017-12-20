package cn.vitco.wx.entity;

/**
 * Created by Sterling on 2017/12/20.
 */
public class TextMessage extends WxBaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
