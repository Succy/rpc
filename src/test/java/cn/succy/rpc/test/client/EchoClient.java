package cn.succy.rpc.test.client;

import cn.succy.rpc.client.BioRpcClient;
import cn.succy.rpc.client.RpcClient;
import cn.succy.rpc.test.api.MyEchoService;

/**
 * @author Succy
 * @date 2017/4/20 17:24
 */
public class EchoClient {
    public static void main(String[] args) {
        RpcClient client = new BioRpcClient();
        MyEchoService service = client.getClientProxy(MyEchoService.class, "echoService1");
        String echo = service.echo("Hello world");

        System.out.println("recv server msg = " + echo);
    }
}
