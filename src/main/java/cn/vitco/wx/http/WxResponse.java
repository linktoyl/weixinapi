package cn.vitco.wx.http;

import cn.vitco.common.Encoding;
import cn.vitco.common.Streams;
import cn.vitco.common.Strings;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 微信返回类
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxResponse {
    public WxResponse(HttpURLConnection conn, Map<String, String> reHeader) throws IOException {
        status = conn.getResponseCode();
        detail = conn.getResponseMessage();
        this.header = reHeader;
    }

    private Map<String, String> header;
    private InputStream stream;
    private int status;
    private String detail;
    private String content;

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isOK() {
        return status == 200;
    }

    public boolean isServerError() {
        return status >= 500 && status < 600;
    }

    public boolean isClientError() {
        return status >= 400 && status < 500;
    }

    void setStream(InputStream stream) {
        this.stream = stream;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * 根据Http头的Content-Type获取网页的编码类型，如果没有设的话则返回null
     */
    public String getEncodeType() {
        String contextType = header.get("Content-Type");
        if (null != contextType) {
            for (String tmp : contextType.split(";")) {
                if (tmp == null)
                    continue;
                tmp = tmp.trim();
                if (tmp.startsWith("charset="))
                    return Strings.trim(tmp.substring(8)).trim();
            }
        }
        return null;
    }

    public InputStream getStream() {
        return new BufferedInputStream(stream);
    }

    public Reader getReader() {
        String encoding = this.getEncodeType();
        if (null == encoding)
            return getReader(Encoding.defaultEncoding());
        else
            return getReader(encoding);
    }

    public Reader getReader(String charsetName) {
        return new InputStreamReader(getStream(), Charset.forName(charsetName));
    }


    public String getContent() {
        if (Strings.isBlank(content)) {
            content = getContent(null);
        }
        return content;
    }

    public String getContent(String charsetName) {
        if (charsetName == null)
            return Streams.readAndClose(getReader());
        return Streams.readAndClose(getReader(charsetName));
    }
}
