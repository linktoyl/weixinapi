package cn.vitco.wx.http;

import cn.vitco.common.Encoding;
import cn.vitco.common.Lang;
import cn.vitco.common.Streams;
import cn.vitco.common.Strings;
import org.apache.http.HttpException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * 微信请求类
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxRequest {

    public static enum METHOD {
        GET, POST, OPTIONS, PUT, DELETE, TRACE, CONNECT, HEAD
    }

    /**
     * 默认连接超时, 30秒
     */
    public static int Default_Conn_Timeout = 30 * 1000;
    /**
     * 默认读取超时, 10分钟
     */
    public static int Default_Read_Timeout = 30 * 1000;

    private String url;
    private METHOD method;
    private String methodString;
    private Map<String, String> header = WxHttp.DEFAULT_HEADERS;
    private Map<String, Object> params = new HashMap<String, Object>();
    private byte[] data;
    private URL cacheUrl;
    private InputStream inputStream;
    private String enc = Encoding.UTF8;
    private boolean offEncode;
    private int timeout = 0;
    protected boolean followRedirects = true;
    private int retry = 1;

    protected HttpURLConnection conn;
    protected SSLSocketFactory sslSocketFactory;

    public WxRequest(String url, METHOD method) {
        this.url = url;
        this.method = method;
    }

    public WxRequest(String url, METHOD method, Map<String, String> header, Map<String, Object> params) {
        this.url = url;
        this.method = method;
        this.header = header;
        this.params = params;
    }

    public WxResponse send() throws HttpException {
        if(!isPost()&&!isGet())
            throw new HttpException("微信Http暂不支持GET和POST外的方法!");

        try {
            openConnection();
            setupRequestHeader();
            if (isPost()) {
                InputStream ins = getInputStream();
                if (ins != null && getHeader() != null && ins instanceof ByteArrayInputStream
                        && getHeader().get("Content-Length") == null)
                    conn.addRequestProperty("Content-Length", "" + ins.available());
                setupDoInputOutputFlag();
                if (null != ins) {
                    OutputStream ops = Streams.buff(conn.getOutputStream());
                    Streams.write(ops, ins);
                    Streams.safeClose(ins);
                    Streams.safeFlush(ops);
                    Streams.safeClose(ops);
                }
            }
            return createResponse(getResponseHeader());
        } catch (Exception e) {
            /*if(retry>0){
                retry--;
                this.url = this.url.replaceFirst("api", "api2");
                cacheUrl = null;
                return send();
            }*/
            throw new HttpException(url, e);
        }

    }

    protected void openConnection() throws Exception {

        URL url = getUrl();
        String host = url.getHost();
        conn = (HttpURLConnection) url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            if (sslSocketFactory != null)
                ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
            else
                ((HttpsURLConnection) conn).setSSLSocketFactory(WxHttp.nopSSLSocketFactory());
        }
        if (!Strings.isBlank(host)) {
            if (url.getPort() > 0 && url.getPort() != 80)
                host += ":" + url.getPort();
            conn.addRequestProperty("Host", host);
        }
        conn.setConnectTimeout(Default_Conn_Timeout);
        if (methodString == null)
            conn.setRequestMethod(method.name());
        else
            conn.setRequestMethod(methodString);
        if (timeout > 0)
            conn.setReadTimeout(timeout);
        else
            conn.setReadTimeout(Default_Read_Timeout);
        conn.setInstanceFollowRedirects(followRedirects);

    }

    protected void setupRequestHeader() {
        if (null != header)
            for (Entry<String, String> entry : header.entrySet())
                conn.addRequestProperty(entry.getKey(), entry.getValue());
    }

    protected WxResponse createResponse(Map<String, String> reHeaders) throws IOException {
        WxResponse rep = null;
        if (reHeaders != null) {
            rep = new WxResponse(conn, reHeaders);
            if (rep.isOK()) {
                InputStream is1 = conn.getInputStream();
                InputStream is2 = null;
                String encoding = conn.getContentEncoding();
                // 如果采用了压缩,则需要处理否则都是乱码
                if (encoding != null && encoding.contains("gzip")) {
                    is2 = new GZIPInputStream(is1);
                } else if (encoding != null && encoding.contains("deflate")) {
                    is2 = new InflaterInputStream(is1, new Inflater(true));
                } else {
                    is2 = is1;
                }

                BufferedInputStream is = new BufferedInputStream(is2);
                rep.setStream(is);
            } else {
                try {
                    rep.setStream(conn.getInputStream());
                } catch (IOException e) {
                    try {
                        rep.setStream(conn.getErrorStream());
                    } catch (Exception e1) {
                        rep.setStream(new InputStream() {
                            @Override
                            public int read() throws IOException {
                                // TODO Auto-generated method stub
                                return -1;
                            }
                        });
                    }
                }
            }
        }
        return rep;
    }

    protected Map<String, String> getResponseHeader() throws IOException {
        if (conn.getResponseCode() < 0)
            throw new IOException("Network error!! resp code=" + conn.getResponseCode());
        Map<String, String> reHeaders = new HashMap<String, String>();
        for (Entry<String, List<String>> en : conn.getHeaderFields().entrySet()) {
            List<String> val = en.getValue();
            if (null != val && val.size() > 0)
                reHeaders.put(en.getKey(), en.getValue().get(0));
        }
        return reHeaders;
    }

    protected void setupDoInputOutputFlag() {
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }

    public InputStream getInputStream() {
        if (inputStream != null) {
            return inputStream;
        } else {
            if (header.get("Content-Type") == null) {
                if (enc == null)
                    enc = Encoding.UTF8;
                header.put("Content-Type", "application/json; encoding=" + enc.toUpperCase());
            }
            if (null == data) {
                try {
                    return new ByteArrayInputStream(getURLEncodedParams().getBytes(enc));
                } catch (UnsupportedEncodingException e) {
                    throw Lang.wrapThrow(e);
                }
            }
            System.out.println(new String(data));
            return new ByteArrayInputStream(data);
        }
    }

    public URL getUrl() throws HttpException {
        if (cacheUrl != null) {
            return cacheUrl;
        }

        StringBuilder sb = new StringBuilder(url);
        try {
            if (this.isGet() && null != params && params.size() > 0) {
                sb.append(url.indexOf('?') > 0 ? '&' : '?');
                sb.append(getURLEncodedParams());
            }
            cacheUrl = new URL(sb.toString());
            return cacheUrl;
        } catch (Exception e) {
            throw new HttpException(sb.toString(), e);
        }
    }

    /**
     * 按URL编码参数
     *
     * @return
     */
    public String getURLEncodedParams() {
        final StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Entry<String, Object> en : params.entrySet()) {
                final String key = en.getKey();
                Object val = en.getValue();
                if (val == null)
                    val = "";
                if (offEncode) {
                    sb.append(key).append('=').append(val).append('&');
                } else {
                    sb.append(WxHttp.encode(key, enc)).append('=').append(WxHttp.encode(val, enc)).append('&');
                }

            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public WxRequest setHeader(Map<String, String> header) {
        if (header == null)
            header = new HashMap<String, String>();
        this.header = header;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public WxRequest setData(String data) {
        try {
            this.data = data.getBytes(Encoding.UTF8);
        }
        catch (UnsupportedEncodingException e) {
            // 不可能
        }
        return this;
    }

    public METHOD getMethod() {
        return method;
    }

    public boolean isGet() {
        return METHOD.GET == method;
    }

    public boolean isPost() {
        return METHOD.POST == method;
    }

    public boolean isDelete() {
        return METHOD.DELETE == method;
    }

    public boolean isPut() {
        return METHOD.PUT == method;
    }

    public WxRequest setMethod(METHOD method) {
        this.method = method;
        return this;
    }

}
