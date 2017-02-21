package cn.succy.rpc.test;

import cn.succy.rpc.comm.net.Request;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author Succy
 * @date 2017-02-19 19:50
 **/

public class TestClassLoader {
    @Test
    public void testClassLoader() {
        InputStream in = ClassLoader.getSystemResourceAsStream("cc.properties");
        if (in == null) {
            System.out.println("null");
        }
    }

    @Test
    public void testGetClassName() {
        Request request = new Request();
        Class<?> clazz = request.getClass();
        System.out.println("class name " + clazz.getName());
    }
}
