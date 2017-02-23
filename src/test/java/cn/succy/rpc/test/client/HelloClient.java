package cn.succy.rpc.test.client;

import cn.succy.rpc.client.RpcClientProxy;
import cn.succy.rpc.comm.RedisServiceDiscoverImpl;
import cn.succy.rpc.test.api.HelloService;

/**
 * @author Succy
 * @date 2017/2/23 10:25
 */
public class HelloClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new RedisServiceDiscoverImpl());
        HelloService service = proxy.getClientProxy(HelloService.class, "hello.service1");
        String msg = service.sayHello("succy");
        System.out.println();
        System.out.println(msg);

    }
}
