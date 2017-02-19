package cn.succy.rpc.comm.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 操作Jedis的工具类
 *
 * @author Succy
 * @date 2017-02-19 17:43
 **/

public class JedisUtils {

    private static String HOST;// Redis服务器地址
    private static int PORT;// 服务端口
    private static int TIMEOUT;// 超时时间：单位ms
    private static int MAX_ACTIVE;// 最大连接数：能够同时建立的“最大链接个数”
    private static int MAX_IDLE;// 最大空闲数：空闲链接数大于maxIdle时，将进行回收
    private static int MIN_IDLE;// 最小空闲数：低于minIdle时，将创建新的链接
    private static int MAX_WAIT;// 最大等待时间：单位ms
    private static boolean TEST_ON_BORROW;// 使用连接时，检测连接是否成功
    private static boolean TEST_ON_RETURN;// 返回连接时，检测连接是否成功

    private static JedisPool pool = null;
    private JedisUtils() {
    }

    static {
        try {
            init();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void init() throws FileNotFoundException {
        InputStream in = ClassLoader.getSystemResourceAsStream("redis.properties");
        if (in == null) {
            throw new FileNotFoundException("Can not found redis properties in classpath!");
        }

        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HOST = properties.getProperty("redis.host");
        PORT = Integer.parseInt(properties.getProperty("redis.port"));
        TIMEOUT = Integer.parseInt(properties.getProperty("redis.timeout"));
        MAX_ACTIVE = Integer.parseInt(properties.getProperty("redis.pool.maxActive"));
        MAX_IDLE = Integer.parseInt(properties.getProperty("redis.pool.maxIdle"));
        MIN_IDLE = Integer.parseInt(properties.getProperty("redis.pool.minIdle"));
        MAX_WAIT = Integer.parseInt(properties.getProperty("redis.pool.maxWait"));
        TEST_ON_BORROW = Boolean.parseBoolean(properties.getProperty("redis.pool.testOnBorrow"));
        TEST_ON_RETURN = Boolean.parseBoolean(properties.getProperty("redis.pool.testOnReturn"));
    }

    /**
     * 获取Jedis的连接池对象
     * @return 如果当前没有创建JedisPool对象则创建，反之直接返回
     */
    public static JedisPool getJedisPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setMinIdle(MIN_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxTotal(MAX_ACTIVE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnReturn(TEST_ON_RETURN);
            pool = new JedisPool(config, HOST, PORT, TIMEOUT);
        }

        return pool;
    }

    /**
     * 从pool里取出一个Jedis对象,在这个版本的Jedis里，废除了returnResource
     * 因此，不考虑通过该方法释放资源，用官方的close方法替代
     * @return jedis 对象
     */
    public static Jedis getJedis() {
        JedisPool jedisPool = getJedisPool();

        return jedisPool.getResource();
    }
}
