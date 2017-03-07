package cn.succy.rpc.client;

import cn.succy.rpc.comm.ServiceDiscover;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.net.Request;
import cn.succy.rpc.comm.net.Response;
import cn.succy.rpc.comm.util.SerializableUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

/**
 * Bio客户端实现，由于客户端只有一个，使用netty作为客户端太过于重量级，导致性能过慢
 * @author Succy
 * @date 2017/2/28 17:48
 */
public class BioRpcClient {
    private String host;
    private int port;
    private ServiceDiscover discover;
    private Response response;

    private static final cn.succy.rpc.comm.log.Logger logger = LoggerFactory.getLogger(BioRpcClient.class);

    public BioRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public BioRpcClient(ServiceDiscover discover) {
        this.discover = discover;
    }

    private void send(Request request) throws Exception {
        Socket socket = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            long openS = System.currentTimeMillis();
            if (host != null && !"".equals(host) && port > 0) {
                socket = new Socket(host, port);
            } else if (discover == null) {
                logger.error("discover is null!");
                throw new RuntimeException("discover is null");
            } else {
                String serviceName = request.getInterfaceName();
                String version = request.getServiceVersion();
                if (version != null && !"".equals(version)) {
                    serviceName += "-" + version;
                }
                String address = this.discover.discover(serviceName);
                if (address != null && !"".equals(address)) {
                    String[] addrArr = address.split(":");
                    if (addrArr.length == 2) {
                        socket = new Socket(addrArr[0], Integer.parseInt(addrArr[1]));
                    }
                }
            }

            if (socket == null) {
                logger.error("can not instance socket");
                throw new Exception("can not instance socket");
            }

            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());
            long s = System.currentTimeMillis();
            byte[] req = SerializableUtils.serialize(request);
            long e = System.currentTimeMillis();
            System.out.println("serialize times: " + (e - s) + " ms");
            // send req to server
            byte[] reqHeader = new byte[4];
            // int --> byte[]
            for (int i = 0; i < 4; ++i) {
                reqHeader[i] = (byte) (req.length >> 8 * (3 - i) & 0xFF);
            }
            long openE = System.currentTimeMillis();
            System.out.println("open socket times: " + (openE - openS) + " ms");

            long sendS = System.currentTimeMillis();
            out.write(reqHeader, 0, 4);
            out.write(req, 0, req.length);
            out.flush();

            // receive from server
            int len = -1;
            byte[] temp = new byte[1024];
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            while ((len = in.read(temp)) != -1) {
                // System.out.println(len);
                resp.write(temp, 0, len);
            }
            byte[] respArr = resp.toByteArray();
            int respLen = respArr.length;
            if (respLen < 4) {
                logger.error("bad response!header length less than 4");
                throw new Exception("bad response!header length less than 4");
            }

            // 提取响应头，响应头存放的是响应体的长度
            int respBodyLen = 0;
            // byte[] --> int
            for (int i = 0; i < 4; i++) {
                respBodyLen += (respArr[i] & 0xFF) << (8 * (3 - i));
            }

            if (respLen - respBodyLen != 4) {
                logger.error("Incomplete response body, expected resp body len is %d, but real len is %d", respBodyLen, (respLen - 4));
                throw new Exception(String.format("Incomplete response body, expected resp body len is %d, but real len is %d", respBodyLen, (respLen - 4)));
            }

            long sendE = System.currentTimeMillis();
            System.out.println("send times: " + (sendE - sendS) + " ms");

            long start = System.currentTimeMillis();
            Response response = SerializableUtils.deserialize(Arrays.copyOfRange(respArr, 4, respLen), Response.class);
            long end = System.currentTimeMillis();
            System.out.println("deserialize times: " + (end - start) + " ms");
            if (response == null) {
                logger.error("bad response, the response is null");
                throw new Exception("bad response, the response is null");
            }
            System.out.println(response);
            setResponse(response);

            resp.close();
        } finally {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
        }
    }

    public <T> T getClientProxy(final Class<T> interfaceCls) {
        return getClientProxy(interfaceCls, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T getClientProxy(final Class<T> interfaceCls, final String version) {
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

                try {
                    send(request);
                } catch (Exception e) {
                    logger.error(e);
                    throw new Exception(e.getMessage());
                }

                return getResponse().getData();
            }
        });
    }// end of getClientProxy

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
