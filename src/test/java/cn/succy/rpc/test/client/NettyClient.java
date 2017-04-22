package cn.succy.rpc.test.client;

import cn.succy.rpc.client.NettyRpcClient;
import cn.succy.rpc.client.RpcClient;
import cn.succy.rpc.test.api.HelloService;
import cn.succy.rpc.test.bean.Student;

/**
 * @author Succy
 * @date 2017/4/22 16:26
 */
public class NettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient("127.0.0.1", 9587);
        HelloService service = client.getClientProxy(HelloService.class, "hello1");
        System.out.println("========== sayHello =========");
        sayHello(service);
        System.out.println("========== echoStudent =========");
        echoStu(service);
    }
    private static void sayHello(HelloService service) {
        long start = System.currentTimeMillis();
        String recv = service.sayHello("你好吗，我是中国人");
        System.out.println("接收到服务器返回信息：" + recv);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }

    private static void echoStu(HelloService service) {
        Student stu = new Student();
        stu.setAddress("广东广州市");
        stu.setAge(32);
        stu.setName("大屌丝");
        stu.setId(1);

        long start = System.currentTimeMillis();
        Student recv = service.echoStudent(stu);
        System.out.println("接收到服务器返回信息：" + recv);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }
}
