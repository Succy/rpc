package cn.succy.rpc.test.client;

import cn.succy.rpc.client.BioRpcClient;
import cn.succy.rpc.comm.RedisServiceDiscoverImpl;
import cn.succy.rpc.test.api.HelloService;

/**
 * @author Succy
 * @date 2017/2/28 19:53
 */
public class BioClient {

    public static void main(String[] args) {
        BioRpcClient client = new BioRpcClient(new RedisServiceDiscoverImpl());

        long start = System.currentTimeMillis();
        HelloService service = client.getClientProxy(HelloService.class, "hello.service1");
        String msg = service.sayHello("我是中国人！！！！");
        System.out.println("msg: " + msg);
        long end = System.currentTimeMillis();

        System.out.println("times: " + (end - start) + " ms");
    }
}
