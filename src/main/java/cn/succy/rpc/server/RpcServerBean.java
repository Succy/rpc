package cn.succy.rpc.server;

import cn.succy.rpc.comm.ServiceRegister;
import cn.succy.rpc.comm.codec.ProtoDecoder;
import cn.succy.rpc.comm.codec.ProtoEncoder;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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

    // 提供带参构造器，可以在spring中使用构造器注入
    public RpcServerBean(String serverAddress, ServiceRegister serviceRegister) {
        this.serverAddress = serverAddress;
        this.serviceRegister = serviceRegister;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 对请求消息进行解码
                            sc.pipeline().addLast(new ProtoDecoder(Request.class));
                            // 对返回给客户端的响应消息进行编码
                            sc.pipeline().addLast(new ProtoEncoder(Response.class));
                            // 加入TCP粘包解码器
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
                            // 将获取到的serviceBean交给服务器处理器处理
                            sc.pipeline().addLast(new RpcServerHandler(handleServiceMap));
                        }
                    });

            // 获取服务器端地址、端口
            if (serverAddress == null || "".equals(serverAddress)) {
                logger.error("service address must be not empty!");
                return;
            }
            String[] addrArr = serverAddress.split(":");
            if (addrArr.length != 2) {
                logger.error("server address information is incomplete!");
                return;
            }
            // 提取主机地址和端口号
            String host = addrArr[0];
            int port = Integer.parseInt(addrArr[1]);
            // 启动服务器
            ChannelFuture future = sb.bind(host, port).sync();
            // 注册服务
            if (serviceRegister != null) {
                for (String serviceName : handleServiceMap.keySet()) {
                    serviceRegister.register(serviceName, serverAddress);
                    logger.info("register service ==> %s", serviceName);
                }
            }

            logger.info("server start on host: %s port: %d", host, port);
            // 同步等待服务器关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅关闭资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化spring配置文件时候会自动调用该方法
     *
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
