package cn.succy.rpc.test.server;

import cn.succy.rpc.comm.annotation.RpcService;
import cn.succy.rpc.test.api.MyEchoService;

/**
 * @author Succy
 * @date 2017/4/20 17:20
 */
@RpcService(value=MyEchoService.class, version="echoService1")
public class MyEchoServiceImpl implements MyEchoService {
    @Override
    public String echo(String msg) {
        System.out.println("msg = " + msg);
        return msg;
    }
}
