package cn.succy.rpc.test.server;

import cn.succy.rpc.server.RpcService;
import cn.succy.rpc.test.api.HelloService;
import cn.succy.rpc.test.bean.Student;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Succy
 * @date 2017-02-25 11:52
 **/
@RpcService(HelloService.class)
public class HelloServiceImpl1 implements HelloService{
    @Override
    public String sayHello(String msg) {
        //PrintWriter pw = null;
        //try {
        //    pw = new PrintWriter("I:\\test1\\data.txt");
        //    pw.println(msg);
        //    pw.flush();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //} finally {
        //    pw.close();
        //}

        return msg;
    }

    @Override
    public Student echoStudent(Student stu) {
        System.out.println(stu);
        return stu;
    }
}
