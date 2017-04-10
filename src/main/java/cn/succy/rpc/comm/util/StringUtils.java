package cn.succy.rpc.comm.util;

/**
 * @author Succy
 * @date 2017/4/10 14:20
 */
public final class StringUtils {
    private StringUtils() {
        throw new AssertionError("util class can not be instance");
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
}
