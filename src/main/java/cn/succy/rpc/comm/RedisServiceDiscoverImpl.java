package cn.succy.rpc.comm;

import cn.succy.rpc.comm.annotation.Component;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.JedisUtils;
import cn.succy.rpc.comm.util.MD5Utils;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用Redis作为存储服务节点信息介质，发现服务的实现类
 *
 * @author Succy
 * @date 2017-02-19 21:04
 **/
@Component
public class RedisServiceDiscoverImpl implements ServiceDiscover {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceDiscoverImpl.class);

    @Override
    public String discover(String serviceName) {
        Jedis jedis = JedisUtils.getJedis();
        String address = "";
        // 获取根key
        String rootKey = MD5Utils.encode(Constant.ROOT_KEY).substring(12, 28);
        String serviceNameKey = MD5Utils.encode(serviceName + serviceName.hashCode()).substring(8, 24);

        if (!jedis.hexists(rootKey, serviceNameKey)) {
            logger.debug("Can not discover serviceNode.serviceName:  %s", serviceName);
            return "";
        }
        String addrListVal = jedis.hget(rootKey, serviceNameKey);
        if (addrListVal != null && !"".equals(addrListVal.trim())) {
            String[] addrArr = addrListVal.split("-");
            int len = addrArr.length;
            if (len == 1) {
                address = addrArr[0];
            } else {
                int index = ThreadLocalRandom.current().nextInt(len);
                address = addrArr[index];
            }

            logger.debug("Discover address: %s by serviceName: %s", address, serviceName);
        }
        // 将jedis对象返还连接池
        jedis.close();
        return address;
    }
}
