package cn.succy.rpc.test;

import cn.succy.rpc.comm.RedisServiceDiscoverImpl;
import cn.succy.rpc.comm.RedisServiceRegisterImpl;
import cn.succy.rpc.comm.ServiceDiscover;
import cn.succy.rpc.comm.ServiceRegister;
import cn.succy.rpc.comm.util.JedisUtils;
import org.junit.Test;

/**
 * @author Succy
 * @date 2017-02-19 20:23
 **/

public class TestJedisUtils {
    @Test
    public void testSetProp() {
        String str = "127.0.0.1:8888";
        String[] split = str.split("-");
        System.out.println(split.length);
        System.out.println(split[0]);
    }

    @Test
    public void testRegister() {
        ServiceRegister register = new RedisServiceRegisterImpl();
        register.register("service1", "127.0.0.1:8080");
        register.register("service1", "192.168.2.1:8880");
        register.register("service1", "128.5.23.129:9658");
        register.register("service1", "56.45.0.1:2354");
        register.register("service1", "12.10.0.1:7456");
    }

    @Test
    public void testDiscover() {
        ServiceDiscover discover = new RedisServiceDiscoverImpl();
        discover.discover("service1");
    }
}
