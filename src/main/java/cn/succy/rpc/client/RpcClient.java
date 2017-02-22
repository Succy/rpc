package cn.succy.rpc.client;

import cn.succy.rpc.comm.codec.ProtoDecoder;
import cn.succy.rpc.comm.codec.ProtoEncoder;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * rpc客户端，用来发送请求和返回请求结果的响应消息，一般该类由代理类调用
 * @author Succy
 * @date 2017/2/22 9:52
 */
public class RpcClient extends ChannelInboundHandlerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private final String host;
    private final int port;
    private Response response;


    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = (Response) msg;
        logger.info("recv from server:[%s]", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    /**
     * 客户端发送请求到服务器
     * @param request 要发送的请求
     * @ 返回响应消息
     */
    public Response send(Request request) {
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
                            sc.pipeline().addLast(RpcClient.this);
                        }
                    });

            ChannelFuture future = bs.connect(host, port).sync();
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
}
