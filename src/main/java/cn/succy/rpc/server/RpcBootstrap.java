package cn.succy.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Rpc服务器启动类，针对使用spring配置的情况
 * @author Succy
 * @date 2017/2/21 12:56
 */
public class RpcBootstrap{
    private String springConfFile;
    public RpcBootstrap(String springConfFile) {
        this.springConfFile = springConfFile;
    }

    /**
     * 开启服务器
     */
    public void start() {
        new ClassPathXmlApplicationContext(springConfFile);
    }
}
