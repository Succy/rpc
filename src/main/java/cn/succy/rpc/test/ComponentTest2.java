package cn.succy.rpc.test;

import cn.succy.rpc.comm.annotation.Component;
import cn.succy.rpc.comm.annotation.Inject;

/**
 * @author Succy
 * @date 2017/4/11 12:10
 */
@Component
public class ComponentTest2 {
    @Inject
    private ComponentTest componentTest;


    public void testCom() {
        System.out.println("测试依赖注入");
        System.out.println("component=" + this.componentTest);
    }

}
