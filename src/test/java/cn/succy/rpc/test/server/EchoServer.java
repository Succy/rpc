package cn.succy.rpc.test.server;

import cn.succy.rpc.server.RpcServer;

/**
 * @author Succy
 * @date 2017/4/20 17:20
 */
public class EchoServer {

    public static void main(String[] args) {
        // 实例化RpcServer
        RpcServer server = new RpcServer();
        // 启动服务器
        server.start();
    }
}