package cn.succy.rpc.test;

import cn.succy.rpc.comm.RedisServiceRegisterImpl;
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
        //System.out.println(JedisUtils.);
    }

    @Test
    public void testRegister() {
        ServiceRegister register = new RedisServiceRegisterImpl();
        register.register("service1", "127.0.0.1:8080");
    }
}
