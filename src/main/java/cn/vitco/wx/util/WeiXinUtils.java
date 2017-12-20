package cn.vitco.wx.util;

import cn.vitco.common.Lang;
import cn.vitco.common.Strings;
import cn.vitco.wx.api.WxHandlerApi;
import cn.vitco.wx.entity.WxEventType;
import cn.vitco.wx.entity.WxInMsg;
import cn.vitco.wx.entity.WxMsgType;
import cn.vitco.wx.entity.WxOutMsg;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sterling on 2017/12/20.
 */
public class WeiXinUtils {

    private final static Logger log = Logger.getLogger("WxApi_Log");
    public static boolean DEV_MODE = true;

    public static void enableDevMode() {
        DEV_MODE = true;
        log.warn("启用开发模式");
    }


    /**
     * 创建一条文本响应
     */
    public static WxOutMsg respText(String to, String content) {
        WxOutMsg out = new WxOutMsg("text");
        out.setContent(content);
        if (to != null)
            out.setToUserName(to);
        return out;
    }

    public static WxOutMsg respText(String content) {
        return respText(null, content);
    }

    /**
     * 根据输入信息,修正发送信息的发送者和接受者
     */
    public static WxOutMsg fix(WxInMsg in, WxOutMsg out) {
        out.setFromUserName(in.getToUserName());
        out.setToUserName(in.getFromUserName());
        out.setCreateTime(System.currentTimeMillis() / 1000);
        return out;
    }

    /**
     * 根据不同的消息类型,调用WxHandler不同的方法
     */
    public static WxOutMsg handle(WxInMsg msg, WxHandlerApi handler) {
        WxOutMsg out = null;
        switch (WxMsgType.valueOf(msg.getMsgType())) {
            case text:
                out = handler.text(msg);
                break;
            case image:
                out = handler.image(msg);
                break;
            case voice:
                out = handler.voice(msg);
                break;
            case video:
                out = handler.video(msg);
                break;
            case location:
                out = handler.location(msg);
                break;
            case link:
                out = handler.link(msg);
                break;
            case event:
                out = handleEvent(msg, handler);
                break;
            case shortvideo:
                out = handler.shortvideo(msg);
                break;
            default:
                log.warn(String.format("暂不支持处理%s微信消息类型", msg.getMsgType()));
                out = handler.defaultMsg(msg);
                break;
        }
        return out;
    }

    /**
     * 根据msg中Event的类型,调用不同的WxHandler方法
     */
    public static WxOutMsg handleEvent(WxInMsg msg, WxHandlerApi handler) {
        WxOutMsg out = null;
        switch (WxEventType.valueOf(msg.getEvent())) {
            case subscribe:
                out = handler.eventSubscribe(msg);
                break;
            case unsubscribe:
                out = handler.eventUnsubscribe(msg);
                break;
            default:
                log.warn(String.format("暂不支持处理%s微信事件类型", msg.getEvent()));
                out = handler.defaultMsg(msg);
                break;
        }
        return out;
    }

    /**
     * 检查signature是否合法
     */
    public static boolean check(String token, String signature, String timestamp, String nonce) {
        // 防范长密文攻击
        if (signature == null
                || signature.length() > 128
                || timestamp == null
                || timestamp.length() > 128
                || nonce == null
                || nonce.length() > 128) {
            log.warn(String.format("检查signature失败: signature=%s,timestamp=%s,nonce=%s",
                    signature,
                    timestamp,
                    nonce));
            return false;
        }
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(token);
        tmp.add(timestamp);
        tmp.add(nonce);
        Collections.sort(tmp);
        String key = Lang.concat("", tmp).toString();
        return Lang.sha1(key).equalsIgnoreCase(signature);
    }

    /**
     * @see #asXml(Writer, WxOutMsg)
     */
    public static String asXml(WxOutMsg msg) {
        if (msg.raw() != null)
            return msg.raw();
        StringWriter sw = new StringWriter();
        asXml(sw, msg);
        return sw.toString();
    }

    public static String cdata(String str) {
        if (Strings.isBlank(str))
            return "";
        return "<![CDATA[" + str.replaceAll("]]", "__") + "]]>";
    }

    public static String tag(String key, String val) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(key).append(">");
        sb.append(val).append("");
        sb.append("</").append(key).append(">\n");
        return sb.toString();
    }

    /**
     * 将一个WxOutMsg转为被动响应所需要的XML文本
     *
     * @param msg
     *            微信消息输出对象
     *
     * @return 输出的 XML 文本
     */
    public static void asXml(Writer writer, WxOutMsg msg) {
        try {
            Writer _out = writer;
            if (DEV_MODE) {
                writer = new StringWriter();
            }
            writer.write("<xml>\n");
            writer.write(tag("ToUserName", cdata(msg.getToUserName())));
            writer.write(tag("FromUserName", cdata(msg.getFromUserName())));
            writer.write(tag("CreateTime", "" + msg.getCreateTime()));
            writer.write(tag("MsgType", cdata(msg.getMsgType())));
            switch (WxMsgType.valueOf(msg.getMsgType())) {
                case text:
                    writer.write(tag("Content", cdata(msg.getContent())));
                    break;
                default:
                    break;
            }
            writer.write("</xml>");
            if (DEV_MODE) {
                String str = writer.toString();
                _out.write(str);
            }
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
