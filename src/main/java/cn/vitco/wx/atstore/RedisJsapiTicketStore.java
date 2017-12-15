package cn.vitco.wx.atstore;

import cn.vitco.wx.api.WxJsapiTicketStoreApi;
import cn.vitco.wx.entity.WxJsapiTicket;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 中央缓存JsapiTicket 底层为redis的实现 需要引入jedis客户端支持
 *
 * Created by Sterling on 2017/12/12.
 */
public class RedisJsapiTicketStore implements WxJsapiTicketStoreApi {

    private final static Logger log = Logger.getLogger("WxApi_Log");
    protected String jsapiKey = "jsapiTicket@jsapiTicket";
    protected JedisCluster jedisCluster;

    public RedisJsapiTicketStore(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public RedisJsapiTicketStore(String jsapiKey, JedisCluster jedisCluster) {
        super();
        this.jsapiKey = jsapiKey;
        this.jedisCluster = jedisCluster;
    }

    public String getJsapiKey() {
        return jsapiKey;
    }

    public void setJsapiKey(String jsapiKey) {
        this.jsapiKey = jsapiKey;
    }


    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public WxJsapiTicket get() {
        try {
            if (jsapiKey == null) {
                throw new RuntimeException("Redis 的key 不能为空!");
            }
            Map<String, String> hash = jedisCluster.hgetAll(jsapiKey);
            if (hash == null || hash.isEmpty()) {
                log.warn(String.format("在redis中未找到有效key为[%s]的 token ", jsapiKey));
                return null;
            }
            WxJsapiTicket jt = new WxJsapiTicket();// 从redis中拿出3个值组装成WxAccessToken返回
            jt.setTicket(hash.get("ticket"));
            jt.setLastCacheTimeMillis(Long.valueOf(hash.get("lastCacheMillis")));
            jt.setExpires(Integer.valueOf(hash.get("expires")));
            log.debug(String.format("微信access_token 从 redis 中取值 key [%s] : \n %s",
                    jsapiKey,
                    JSONObject.toJSON(jt)));
            return jt;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // jedisPool.returnResource(jedis); //这是老版本归还连接的方法 已经deprecated
            try {
                jedisCluster.close();// 2.9.0的方法直接close
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(String ticket, int expires, long lastCacheTimeMillis) {

        try {
            if (jsapiKey == null) {
                throw new RuntimeException("Redis access_token key should not be null!");
            }
            Map<String, String> hash = new HashMap<String, String>();
            hash.put("ticket", ticket);// 存入token值
            hash.put("lastCacheMillis", String.valueOf(lastCacheTimeMillis));// 存入设置的过期时间
            hash.put("expires", String.valueOf(expires));// 存入当前缓存时间
            String result = jedisCluster.hmset(jsapiKey, hash);
            jedisCluster.expire(jsapiKey, expires);
            log.info(String.format("新的access_token生成存储redis key [%s] , redis 返回: %s",
                    jsapiKey,
                    result));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
