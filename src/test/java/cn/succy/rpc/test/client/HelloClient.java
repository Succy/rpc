package cn.succy.rpc.test.client;

import cn.succy.rpc.client.RpcClientProxy;
import cn.succy.rpc.comm.RedisServiceDiscoverImpl;
import cn.succy.rpc.test.api.HelloService;
import cn.succy.rpc.test.bean.Student;

/**
 * @author Succy
 * @date 2017/2/23 10:25
 */
public class HelloClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new RedisServiceDiscoverImpl());
        HelloService service = proxy.getClientProxy(HelloService.class, "hello.service1");
        String msg = service.sayHello("中国汉字");
        // System.out.println();
        System.out.println(msg);


        for (int i = 0; i < 100; i++) {

            Student stu = new Student();
            stu.setId(1000+i);
            stu.setName("张全蛋"+i);
            stu.setAge(25);
            stu.setAddress("广东省广州市海珠区工业大道北" + i + "号");
            Student result = service.echoStudent(stu);
            System.out.println("============echo student==========");
            System.out.println(result);
        }

    }
}
