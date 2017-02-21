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

import java.util.HashMap;
import java.util.Map;

/**
 * Rpc服务器
 *
 * @author Succy
 * @date 2017/2/21 19:56
 */
public class RpcServer {
    private String host;
    private int port;
    private ServiceRegister register;

    private Map<String, Object> handleServiceMap;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer(String host, int port, ServiceRegister register) {
        this.host = host;
        this.port = port;
        this.register = register;
        handleServiceMap = new HashMap<>();
    }

    /**
     * 注册服务
     * @param target 要注册的服务实现类
     * @param version 服务版本号
     * @return
     */
    public RpcServer registerService(Object target, String version) {
        if (target == null) {
            logger.error("target service is not null.");
            return null;
        }
        Class<?> targetCls = target.getClass();
        String serviceName = targetCls.getName();

        if (version != null && !"".equals(version.trim())) {
            serviceName += "-" + version;
        }
        String serviceAddress = host + ":" + port;
        try {
            register.register(serviceName, serviceAddress);
        } catch (NullPointerException e) {
            logger.error("register is null", e);
            return null;
        }
        handleServiceMap.put(serviceName, target);
        return this;
    }

    /**
     * 启动服务器
     */
    public void start() {
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
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            // 将获取到的serviceBean交给服务器处理器处理
                            sc.pipeline().addLast(new RpcServerHandler(handleServiceMap));
                        }
                    });

            // 启动服务器
            ChannelFuture future = sb.bind(host, port).sync();

            logger.info("server start on host: %s port: %d", host, port);
            // 同步等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("an exception occur when server start", e);
        } finally {
            // 优雅关闭资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
