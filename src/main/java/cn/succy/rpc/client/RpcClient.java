package cn.succy.rpc.client;

public interface RpcClient {

    <T> T getClientProxy(final Class<T> interfaceCls, final String version);

    <T> T getClientProxy(final Class<T> interfaceCls);
}
