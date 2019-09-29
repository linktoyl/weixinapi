package cn.vitco.common;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HttpUtils {
    public static String post_String(String SERVER_URL,
                                     String json, HashMap<String, String> reqHeader)
            throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(SERVER_URL);

        // 设置请求的header
        if (reqHeader != null) {
            for (String key : reqHeader.keySet()) {
                post.addHeader(key, reqHeader.get(key));
            }
        }
        // 设置请求参数
        StringEntity params = new StringEntity(json.toString(),"utf-8");
        post.setEntity(params);
        // 执行请求
        HttpResponse response = httpclient.execute(post);
        System.out.println("返回状态码：" + response.getStatusLine().getStatusCode());
        String responseEntity = EntityUtils.toString(response.getEntity(),
                "utf-8");
        return responseEntity;
        /*
         * // 判断是否发送成功，发送成功返回true String code =
         * JSON.parseObject(responseEntity).getString("code"); if
         * (code.equals("200")) { return "success"; } return "error";
         */
    }
    public static String sendMsg(String SERVER_URL, HashMap<String, Object> reqParams,HashMap<String, String> reqHeader)
            throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(SERVER_URL);

        //设置请求的header
        if (reqHeader != null) {
            for (String key : reqHeader.keySet()) {
                post.addHeader(key, reqHeader.get(key));
            }
        }
        // 设置请求参数
        if (reqParams != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key : reqParams.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, reqParams.get(key)
                        + ""));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        }
        // 执行请求
        HttpResponse response = httpclient.execute(post);
        System.out.println("返回状态码："+response.getStatusLine().getStatusCode());
        String responseEntity = EntityUtils.toString(response.getEntity(),
                "utf-8");
        return responseEntity;
		/*// 判断是否发送成功，发送成功返回true`
		String code = JSON.parseObject(responseEntity).getString("code");
		if (code.equals("200")) {
			return "success";
		}
		return "error";*/
    }
}
