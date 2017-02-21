package cn.succy.rpc.test;

import cn.succy.rpc.comm.RedisServiceRegisterImpl;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.server.RpcServer;
import org.junit.Test;

/**
 * @author Succy
 * @date 2017/2/21 20:31
 */
public class TestRpcServer {

    @Test
    public void testServer() {
        RpcServer server = new RpcServer("127.0.0.1", 9851, new RedisServiceRegisterImpl());
        server.registerService(new Request(), "lllll");
    }
}
