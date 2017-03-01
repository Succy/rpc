package cn.succy.rpc.comm;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.JedisUtils;
import cn.succy.rpc.comm.util.MD5Utils;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Redis作为存储服务节点信息介质，注册服务的实现类
 *
 * @author Succy
 * @date 2017-02-19 21:10
 **/

public class RedisServiceRegisterImpl implements ServiceRegister {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceRegisterImpl.class);
    private static final Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

    @Override
    public void register(String serviceName, String address) {
        Jedis jedis = JedisUtils.getJedis();

        // 获取根key
        String rootKey = MD5Utils.encode(Constant.ROOT_KEY).substring(12, 28);
        // 作为hash的key
        String serviceNameKey = MD5Utils.encode(serviceName + serviceName.hashCode()).substring(8, 24);
        String registerAddr = isExistAddress(serviceNameKey, address);
        if (null != registerAddr) {
            jedis.hset(rootKey, serviceNameKey, registerAddr);
            logger.debug("Register address to service on serviceNode: %s, address: %s", serviceNameKey, address);
        } else {
            logger.debug("The address: %s had been register on serviceNode: %s", address, serviceNameKey);
        }


        jedis.close();
    }

    /**
     * 校验一下要插入的地址是不是已经存在，避免重复
     *
     * @param serviceNameKey 对应的服务名字key
     * @param address        要插入的地址
     * @return 如果存在有参数中的地址，返回true,反之返回false
     */
    private String isExistAddress(String serviceNameKey, String address) {

        if (cacheMap.containsKey(serviceNameKey)) {
            String addrListVal = cacheMap.get(serviceNameKey);
            String[] addrArr = addrListVal.split("-");
            for (String s : addrArr) {
                if (s.equals(address)) {
                    return null;
                }
            }
            // 遍历结束还没有找到对应的address，说明这是一个新的address，添加进去
            addrListVal += "-" + address;
            cacheMap.put(serviceNameKey, addrListVal);
            return addrListVal;
        }
        // 如果不存在，则添加进去
        cacheMap.put(serviceNameKey, address);
        return address;
    }
}
