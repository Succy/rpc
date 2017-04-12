package cn.succy.rpc.comm;

/**
 * 服务注册接口，抽象成接口，方便日后使用其他服务注册/发现组件做扩展
 * @author Succy
 * @date 2017-02-19 15:57
 */
public interface ServiceRegister {
    void register(String serviceName, String address);
}
