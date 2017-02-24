package cn.succy.rpc.test.api;

import cn.succy.rpc.test.bean.Student;

/**
 * @author Succy
 * @date 2017/2/23 10:14
 */
public interface HelloService {
    String sayHello(String msg);
    Student echoStudent(Student stu);
}
