package cn.vitco.cache.redis;

import cn.vitco.cache.redis.exception.VtRedisException;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Sterling on 2017/12/12.
 */
public class JedisClusterFactory {
    private static String addressConfig;
    private static String addressKeyPrefix ;
    private static String password;

    private static JedisCluster jedisCluster;
    private static JedisPoolConfig config;

    private static Integer timeout = 30;
    private static Integer soTimeout = 30;
    private static Integer maxAttempts = 8;


    static {
        try {
            Properties prop = new Properties();
            prop.load(JedisClusterFactory.class.getClassLoader().getResourceAsStream("redis.properties"));
            //创建jedis池配置实例
            config = new JedisPoolConfig();
            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(prop.getProperty("jedis.pool.maxTotal", "500")));
            config.setMaxIdle(Integer.valueOf(prop.getProperty("jedis.pool.maxIdle", "50")));
            config.setMinIdle(Integer.valueOf(prop.getProperty("jedis.pool.minIdle", "10")));
            config.setMaxWaitMillis(Long.valueOf(prop.getProperty("jedis.pool.maxWait", "100000")));
            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("jedis.pool.testOnBorrow", "true")));
            config.setTestOnReturn(Boolean.valueOf(prop.getProperty("jedis.pool.testOnReturn", "true")));

            timeout = Integer.valueOf(prop.getProperty("jedis.timeout", "30"));
            soTimeout = Integer.valueOf(prop.getProperty("jedis.soTimeout", "30"));
            maxAttempts = Integer.valueOf(prop.getProperty("jedis.maxAttempts", "8"));

            addressConfig = prop.getProperty("jedis.ip.config", "redis-address.properties");
            addressKeyPrefix = prop.getProperty("jedis.ip.prefix", "address");
            password = prop.getProperty("jedis.password", "");
            Set<HostAndPort> haps = parseHostAndPort();

            /**
             * 创建 JedisCluster 实例
             */
            if(password == null || password.equals("")){
                jedisCluster = new JedisCluster(haps, timeout, maxAttempts,config);
            }else{
                jedisCluster = new JedisCluster(haps, timeout, soTimeout, maxAttempts, password, config);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取cluster实例
     * @return
     */
    public JedisCluster getJedisCluster(){
        return jedisCluster;
    }

    /**
     * 读取redis集群地址
     * @return
     * @throws VtRedisException
     */
    private static Set<HostAndPort> parseHostAndPort() throws VtRedisException {
        Pattern ipPattern = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
        try {
            Properties prop = new Properties();
            prop.load(JedisClusterFactory.class.getClassLoader().getResourceAsStream(addressConfig));

            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for (Object key : prop.keySet()) {

                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }

                String val = (String) prop.get(key);

                boolean isIpPort = ipPattern.matcher(val).matches();

                if (!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                String[] ipAndPort = val.split(":");

                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new VtRedisException("解析 jedis 配置文件失败", ex);
        }
    }
}
