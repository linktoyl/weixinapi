package cn.vitco.wx.atstore;

import cn.vitco.common.Strings;
import cn.vitco.wx.api.WxAccessTokenStoreApi;
import cn.vitco.wx.entity.WxAccessToken;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 中央缓存access_token 底层为redis的实现 需要引入jedis客户端支持
 *
 * Created by Sterling on 2017/12/12.
 */
public class RedisAccessTokenStore implements WxAccessTokenStoreApi {

    private final static Logger log = Logger.getLogger("WxApi_Log");
    protected String tokenKey = "accessToken@accessToken";
    protected JedisCluster jedisCluster;

    public RedisAccessTokenStore() {}

    public RedisAccessTokenStore(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public RedisAccessTokenStore(String tokenKey, JedisCluster jedisCluster) {
        if (!Strings.isBlank(tokenKey))
            this.tokenKey = tokenKey;
        this.jedisCluster = jedisCluster;
    }


    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    @Override
    public WxAccessToken get() {

        try {
            if (tokenKey == null) {
                throw new RuntimeException("Redis 的key 不能为空!");
            }
            Map<String, String> hash = jedisCluster.hgetAll(tokenKey);
            if (hash == null || hash.isEmpty()) {
                log.warn(String.format("在redis中未找到有效key为[%s]的 token ", tokenKey));
                return null;
            }
            WxAccessToken at = new WxAccessToken();// 从redis中拿出3个值组装成WxAccessToken返回
            at.setToken(hash.get("token"));
            at.setLastCacheTimeMillis(Long.valueOf(hash.get("lastCacheMillis")));
            at.setExpires(Integer.valueOf(hash.get("expires")));
            log.debug(String.format("微信access_token 从 redis 中取值 key [%s] : \n %s",
                    tokenKey,
                    JSONObject.toJSON(at)));
            return at;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // jedisPool.returnResource(jedis); //这是老版本归还连接的方法 已经deprecated
            /*try {
                jedisCluster.close();// 2.9.0的方法直接close
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        return null;
    }

    @Override
    public void save(String token, int expires, long lastCacheTimeMillis) {
        try {
            if (tokenKey == null) {
                throw new RuntimeException("Redis access_token key should not be null!");
            }
            Map<String, String> hash = new HashMap<String, String>();
            hash.put("token", token);// 存入token值
            hash.put("lastCacheMillis", String.valueOf(lastCacheTimeMillis));// 存入设置的过期时间
            hash.put("expires", String.valueOf(expires));// 存入当前缓存时间
            String result = jedisCluster.hmset(tokenKey, hash);
            jedisCluster.expire(tokenKey, expires);
            log.info(String.format("新的access_token生成存储redis key [%s] , redis 返回: %s",
                    tokenKey,
                    result));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            /*try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

}
