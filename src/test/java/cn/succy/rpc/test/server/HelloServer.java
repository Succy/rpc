package cn.succy.rpc.test.server;

import cn.succy.rpc.comm.RedisServiceRegisterImpl;
import cn.succy.rpc.server.RpcServer;
import cn.succy.rpc.test.api.HelloService;

/**
 * @author Succy
 * @date 2017/2/23 10:18
 */
public class HelloServer {
    public static void main(String[] args) {
        RpcServer server = new RpcServer("127.0.0.1", 9568, new RedisServiceRegisterImpl());
        HelloService service = new HelloServiceImpl();
        server.registerService(HelloService.class, service, "hello.service1")
                .registerService(HelloService.class, service, "student.service1").start();

    }
}
