package cn.succy.rpc.server;

import cn.succy.rpc.comm.ServiceRegister;
import cn.succy.rpc.comm.annotation.Component;
import cn.succy.rpc.comm.annotation.RpcService;
import cn.succy.rpc.comm.codec.ProtoDecoder;
import cn.succy.rpc.comm.codec.ProtoEncoder;
import cn.succy.rpc.comm.kit.BeanKit;
import cn.succy.rpc.comm.kit.PropsKit;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc服务器
 *
 * @author Succy
 * @date 2017/2/21 19:56
 */
@Component
public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private static final String host = PropsKit.getServerHost();
    private static final int port = PropsKit.getServerPort();
    private static final Map<Class<?>, Object> bwaMap = BeanKit.getBeansMapWithAnnotation(RpcService.class);
    private static final Map<String, Object> handleServiceMap = new ConcurrentHashMap<>();
    private static final ServiceRegister register = (ServiceRegister) BeanKit.getBean(PropsKit.getServiceRegisterClass());

    static {
        if (bwaMap != null && !bwaMap.isEmpty()) {
            for (Object bean : bwaMap.values()) {
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if (version != null && !"".equals(version.trim())) {
                    serviceName = serviceName + "-" + version;
                }
                String serviceAddress = host + ":" + port;
                // 注册服务
                register.register(serviceName, serviceAddress);
                handleServiceMap.put(serviceName, bean);
            }
        }
    }

    public RpcServer() {
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
