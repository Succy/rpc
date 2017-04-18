package cn.succy.rpc.client;

import cn.succy.rpc.comm.net.Request;

public interface RpcClient {
    //void send(Request request) throws Exception;

    <T> T getClientProxy(final Class<T> interfaceCls, final String version);

    <T> T getClientProxy(final Class<T> interfaceCls);
}
