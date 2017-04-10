package cn.succy.rpc.test.util;

import cn.succy.rpc.comm.util.ClassUtils;
import org.junit.Test;

/**
 * @author Succy
 * @date 2017/4/10 14:00
 */
public class UtilTest {
    @Test
    public void testClassUtils() {
        ClassUtils.getClassSet("cn.succy.comm.util");
    }
}
