package cn.succy.rpc.comm.util;

import cn.succy.rpc.comm.kit.PropsKit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 操作Jedis的工具类
 *
 * @author Succy
 * @date 2017-02-19 17:43
 **/

public final class JedisUtils {

    private static JedisPool pool = null;

    private JedisUtils() {
        throw new AssertionError("utils class can not be instance");
    }

    /**
     * 获取Jedis的连接池对象
     *
     * @return 如果当前没有创建JedisPool对象则创建，反之直接返回
     */
    public static JedisPool getJedisPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(PropsKit.getRedisPoolMaxIdle());
            config.setMinIdle(PropsKit.getRedisPoolMinIdle());
            config.setMaxWaitMillis(PropsKit.getRedisPoolMaxWait());
            config.setMaxTotal(PropsKit.getRedisPoolMaxActive());
            config.setTestOnBorrow(PropsKit.getRedisPoolTestOnBorrow());
            config.setTestOnReturn(PropsKit.getRedisPoolTestOnReturn());
            pool = new JedisPool(config, PropsKit.getRedisHost(), PropsKit.getRedisPort(), PropsKit.getRedisTimeout());
        }

        return pool;
    }

    /**
     * 从pool里取出一个Jedis对象,在这个版本的Jedis里，废除了returnResource
     * 因此，不考虑通过该方法释放资源，用官方的close方法替代
     *
     * @return jedis 对象
     */
    public static Jedis getJedis() {
        JedisPool jedisPool = getJedisPool();

        return jedisPool.getResource();
    }
}
