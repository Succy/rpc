package cn.succy.rpc.test;

import cn.succy.rpc.comm.util.MD5Utils;
import org.junit.Test;

import java.security.spec.ECField;

/**
 * @author Succy
 * @date 2017-02-19 21:15
 **/

public class TestMD5Utils {
    @Test
    public void testMD5Encode() {
        String key = "service1";
        String encode = MD5Utils.encode(key + key.hashCode());
        System.out.println(encode);
        String substring = encode.substring(8, 24);
        System.out.println(substring);
        String subEncode = MD5Utils.encode(substring);
        System.out.println(subEncode);
        // 1f15b294e6668fc20b2f4b6189eb7714
    }
}
