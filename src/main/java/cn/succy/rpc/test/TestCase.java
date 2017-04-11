package cn.succy.rpc.test;

import cn.succy.rpc.comm.ClassPathPropsAppContext;
import cn.succy.rpc.comm.annotation.Component;
import org.junit.Test;

import java.util.Map;

/**
 * @author Succy
 * @date 2017/4/11 14:24
 */
public class TestCase {
    @Test
    public void testBeanKit() {
        ClassPathPropsAppContext ctx = new ClassPathPropsAppContext();
        ComponentTest2 bean = ctx.getBean(ComponentTest2.class);
        Map<Class<?>, Object> beans = ctx.getBeansWithAnnotation(Component.class);
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key=" + key + "  value=" + value);
        }
    }

}
