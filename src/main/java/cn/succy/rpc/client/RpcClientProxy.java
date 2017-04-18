package cn.succy.rpc.client;

import cn.succy.rpc.comm.ServiceDiscover;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import cn.succy.rpc.comm.util.Constant;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Rpc客户端代理类，该类对于用户是可见的，在这一层，用户就像直接调用服务端的方法一样
 * 通过代理对象去调用方法即可，由于客户端与服务端都使用同一套接口的api作为方法的约束
 * 在客户端，只需要通过指定请求的服务端目标方法所在的接口，并指定服务的版本，在服务器
 * 端可以同一个接口有多个实现类，只要指定服务版本即可。
 *
 * @author Succy
 * @date 2017/2/22 9:52
 */
public class RpcClientProxy {
    //private String host;
    //private int port;
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private final ServiceDiscover discover;

    private boolean isSuccess;
    private Response response;


    public RpcClientProxy(ServiceDiscover discover) {
        //this.host = host;
        //this.port = port;
        this.discover = discover;
    }

    public <T> T getClientProxy(final Class<?> interfaceCls) {
        return getClientProxy(interfaceCls, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T getClientProxy(final Class<?> interfaceCls, final String version) {
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
                if (discover == null) {
                    logger.error("discover must be not null");
                    throw new RuntimeException("discover is null!");
                }
                if (version != null && !"".equals(version.trim())) {
                    serviceName += "-" + version;
                }
                String serverAddress = discover.discover(serviceName);
                if (!"".equals(serverAddress.trim())) {
                    String[] addrArr = serverAddress.split(":");
                    if (addrArr.length == 2) {
                        String host = addrArr[0];
                        int port = Integer.parseInt(addrArr[1]);
                        NettyRpcClient client = new NettyRpcClient(host, port);
                        Response resp = client.send(request);
                        if (resp == null) {
                            logger.error("receive response fail");
                            throw new RuntimeException("receive response fail");
                        } else {
                            // 响应表示成功
                            if (resp.getRespCode() == Constant.RespCode.OK) {
                                RpcClientProxy.this.isSuccess = true;
                                RpcClientProxy.this.response = resp;
                                return resp.getData();
                            }
                        }
                    }
                }
                return null;
            }
        });
    }// end of getClientProxy

    // Getter
    public boolean isSuccess() {
        return isSuccess;
    }

    public Response getResponse() {
        return response;
    }
}
