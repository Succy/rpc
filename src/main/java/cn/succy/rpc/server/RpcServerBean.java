package cn.succy.rpc.server;

import cn.succy.rpc.comm.ServiceRegister;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持Spring配置的服务器端bean
 *
 * @author Succy
 * @date 2017-02-20 22:52
 **/

public class RpcServerBean implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerBean.class);
    private String serverAddress;// 服务器端地址，采用ip:port这种形式
    private ServiceRegister serviceRegister;// 服务注册器

    // serviceName --> serviceBean
    private Map<String, Object> handleServiceMap = new HashMap<>();

    // 提供代参构造器，可以在spring中使用构造器注入
    public RpcServerBean(String serverAddress, ServiceRegister serviceRegister) {
        this.serverAddress = serverAddress;
        this.serviceRegister = serviceRegister;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     *初始化spring配置文件时候会自动调用该方法
     * @param ctx
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // 通过ApplicationContext获取所有注解有@RpcService的bean
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap != null && !serviceBeanMap.isEmpty()) {
            for (Object bean : serviceBeanMap.values()) {
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if (version != null && !"".equals(version.trim())) {
                    serviceName = serviceName + "-" + version;
                }

                handleServiceMap.put(serviceName, bean);
            }
        }
    }

    // 提供setter，在spring中提供setter注入
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }
}
