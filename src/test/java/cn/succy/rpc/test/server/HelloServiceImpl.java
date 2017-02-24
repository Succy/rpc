package cn.succy.rpc.test.server;

import cn.succy.rpc.test.api.HelloService;
import cn.succy.rpc.test.bean.Student;

/**
 * @author Succy
 * @date 2017/2/23 10:15
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String msg) {
        System.out.println("hello " + msg);
        return msg;
    }

    @Override
    public Student echoStudent(Student stu) {
        System.out.println(stu.toString());
        return stu;
    }
}
