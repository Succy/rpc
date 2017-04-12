package cn.succy.rpc.test.server;

import cn.succy.rpc.server.RpcServer;

/**
 * @author Succy
 * @date 2017/2/23 10:18
 */
public class HelloServer {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.start();

    }
}
