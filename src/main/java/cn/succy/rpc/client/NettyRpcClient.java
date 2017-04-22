package cn.succy.rpc.client;

import cn.succy.rpc.comm.ServiceDiscover;
import cn.succy.rpc.comm.codec.ProtoDecoder;
import cn.succy.rpc.comm.codec.ProtoEncoder;
import cn.succy.rpc.comm.kit.BeanKit;
import cn.succy.rpc.comm.kit.PropsKit;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.StringUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * rpc客户端，用来发送请求和返回请求结果的响应消息，一般该类由代理类调用
 *
 * @author Succy
 * @date 2017/2/22 9:52
 */
@ChannelHandler.Sharable
public class NettyRpcClient extends ChannelInboundHandlerAdapter implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private static final ServiceDiscover discover = (ServiceDiscover) BeanKit.getBean(PropsKit.getServiceDiscoverClass());
    private Response response;
    private String host;
    private int port;


    public NettyRpcClient() {
    }


    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = (Response) msg;
        logger.debug("recv from server:[%s]", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    /**
     * 客户端发送请求到服务器
     *
     * @param request 要发送的请求
     * @ 返回响应消息
     */
    private Response send(Request request, String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new ProtoDecoder(Response.class));
                            sc.pipeline().addLast(new ProtoEncoder(Request.class));
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            sc.pipeline().addLast(NettyRpcClient.this);
                        }
                    });

            ChannelFuture future = bs.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            future.channel().closeFuture().sync();
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getClientProxy(Class<T> interfaceCls, final String version) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class<?>[]{interfaceCls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = new Request();
                String requestId = UUID.randomUUID().toString();
                request.setRequestId(requestId);
                request.setInterfaceName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParamTypes(method.getParameterTypes());
                request.setParams(args);
                request.setServiceVersion(version);

                String serviceName = request.getInterfaceName();
                if (StringUtils.isEmpty(host) || port == 0) {
                    if (discover == null) {
                        logger.error("discover must be not null");
                        throw new RuntimeException("discover is null!");
                    }
                    if (!StringUtils.isEmpty(version)) {
                        serviceName += "-" + version;
                    }
                    String serverAddress = discover.discover(serviceName);
                    if (!"".equals(serverAddress.trim())) {
                        String[] addrArr = serverAddress.split(":");
                        if (addrArr.length == 2) {
                            host = addrArr[0];
                            port = Integer.parseInt(addrArr[1]);
                        }
                    } else {
                        // host port没有值，抛异常
                        logger.error("host not found error");
                        throw new RuntimeException("host not found exception");
                    }
                }

                Response resp = send(request, host, port);
                if (resp == null) {
                    logger.error("receive response fail");
                    throw new RuntimeException("receive response fail");
                } else {
                    // 响应表示成功
                    if (resp.getRespCode() == Constant.RespCode.OK) {
                        return resp.getData();
                    }
                }
                return null;
            }
        });
    }

    @Override
    public <T> T getClientProxy(Class<T> interfaceCls) {
        return getClientProxy(interfaceCls, "");
    }
}
