package cn.succy.rpc.comm;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.JedisUtils;
import cn.succy.rpc.comm.util.MD5Utils;
import redis.clients.jedis.Jedis;

/**
 * 使用Redis作为存储服务节点信息介质，注册服务的实现类
 *
 * @author Succy
 * @date 2017-02-19 21:10
 **/

public class RedisServiceRegisterImpl implements ServiceRegister {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceRegisterImpl.class);

    @Override
    public void register(String serviceName, String address) {
        Jedis jedis = JedisUtils.getJedis();

        // 获取根key
        String rootKey = MD5Utils.encode(Constant.ROOT_KEY).substring(12, 28);
        // 作为hash的key
        String serviceNameKey = MD5Utils.encode(serviceName + serviceName.hashCode()).substring(8, 24);
        if (!jedis.hexists(rootKey, serviceNameKey)) {
            jedis.hset(rootKey, serviceNameKey, address);
            logger.debug("Create service node. rootKey: %s, serviceNode: %s", rootKey, serviceNameKey);
        } else {
            String addrListVal = jedis.hget(rootKey, serviceNameKey);
            if (!isExistAddress(addrListVal, address)) {

                addrListVal += "-" + address;
                jedis.hset(rootKey, serviceNameKey, addrListVal);
                logger.debug("Register address to service on serviceNode: %s, address: %s", serviceNameKey, address);
            } else {
                logger.debug("The address: %s had been register on serviceNode: %s", address, serviceNameKey);
            }
        }

        jedis.close();
    }

    /**
     * 校验一下要插入的地址是不是已经存在，避免重复
     *
     * @param addrListVal 获取出来的地址列表
     * @param address     要插入的地址
     * @return
     */
    private boolean isExistAddress(String addrListVal, String address) {

        String[] addrArr = addrListVal.split("-");
        for (String s : addrArr) {
            if (s.equals(address)) {
                return true;
            }
        }
        return false;
    }
}
