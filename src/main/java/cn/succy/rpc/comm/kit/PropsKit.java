package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.PropsUtils;
import cn.succy.rpc.comm.util.StringUtils;

import java.util.Properties;

/**
 * 操作配置文件的助手类
 *
 * @author Succy
 * @date 2017/4/10 17:38
 */
public final class PropsKit {
    private static Properties PROPS = PropsUtils.loadProps(Constant.Prop.CONF_FILE);

    /**
     * 提供一个可以指定配置文件的方法
     *
     * @param propsFileName
     */
    public static void use(String propsFileName) {
        if (!StringUtils.isEmpty(propsFileName)) {
            PROPS = PropsUtils.loadProps(propsFileName);
        }
    }

    public static String getBaseScanPackage() {
        return PropsUtils.getString(PROPS, Constant.Prop.BASE_SCAN_PACKAGE);
    }

    public static String getRedisHost() {
        return PropsUtils.getString(PROPS, Constant.Prop.REDIS_HOST, "127.0.0.1");
    }

    public static int getRedisPort() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_PORT, 6379);
    }

    public static int getRedisTimeout() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_TIMEOUT, 3000);
    }

    public static int getRedisPoolMaxActive() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_POOL_MAXACTIVE, 100);
    }

    public static int getRedisPoolMaxIdle() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_POOL_MAXIDLE, 20);
    }

    public static int getRedisPoolMinIdle() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_POOL_MINIDLE, 5);
    }

    public static int getRedisPoolMaxWait() {
        return PropsUtils.getInt(PROPS, Constant.Prop.REDIS_POOL_MAXWAIT, 3000);
    }

    public static boolean getRedisPoolTestOnBorrow() {
        return PropsUtils.getBoolean(PROPS, Constant.Prop.REDIS_POOL_TESTONBORROW, false);
    }

    public static boolean getRedisPoolTestOnReturn() {
        return PropsUtils.getBoolean(PROPS, Constant.Prop.REDIS_POOL_TESTONRETURN, false);
    }

    public static String getServerHost() {
        return PropsUtils.getString(PROPS, Constant.Prop.SERVER_HOST, Constant.DEFAULT_SERVER_HOST);
    }

    public static int getServerPort() {
        return PropsUtils.getInt(PROPS, Constant.Prop.SERVER_PORT, Constant.DEFAULT_SERVER_PORT);
    }

    public static String getServiceDiscoverClass() {
        return PropsUtils.getString(PROPS, Constant.Prop.SERVICE_DISCOVER_CLASS, Constant.DEFAULT_SERVICE_DISCOVER_CLASS);
    }

    public static String getServiceRegisterClass() {
        return PropsUtils.getString(PROPS, Constant.Prop.SERVICE_REGISTER_CLASS, Constant.DEFAULT_SERVICE_REGISTER_CLASS);
    }
}
