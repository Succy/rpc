package cn.succy.rpc.server;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.ReflectionUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Rpc服务器端处理器
 *
 * @author Succy
 * @date 2017-02-20 22:56
 **/

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handleServiceMap;

    public RpcServerHandler(Map<String, Object> handleServiceMap) {
        this.handleServiceMap = handleServiceMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        Response resp = new Response();
        resp.setRequestId(request.getRequestId());
        try {
            Object obj = handleRequest(request);
            int respCode = Constant.RespCode.OK;
            resp.setData(obj);
            resp.setRespCode(respCode);
            resp.setMsg(Constant.RespMsg.getMsg(respCode));
        }catch (Exception e) {
            int respCode = Constant.RespCode.ERROR;
            resp.setMsg(Constant.RespMsg.getMsg(respCode));
            resp.setRespCode(respCode);
            logger.error("server handle request error, error msg: %s", Constant.RespMsg.getMsg(respCode));
        }
        // 响应客户端，并关闭于客户端的连接
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    /**
     * 处理请求的方法，使用反射去执行要执行的方法
     * @param req 要处理的请求
     * @return 返回执行客户端请求的服务端的方法的结果
     * @throws Exception
     */
    private Object handleRequest(Request req)throws Exception {
        String serviceName = req.getInterfaceName();
        String serviceVersion = req.getServiceVersion();

        if (serviceVersion != null && !"".equals(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }

        Object service = handleServiceMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("Can not found service bean by serviceName: " + serviceName);
        }
        Class<?> serviceClass = service.getClass();
        String methodName = req.getMethodName();
        Class<?>[] paramTypes = req.getParamTypes();
        Object[] params = req.getParams();

        Method method = serviceClass.getDeclaredMethod(methodName, paramTypes);

        return ReflectionUtils.invokeMethod(service, method, params);
    }
}
