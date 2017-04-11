package cn.succy.rpc.comm.util;

/**
 * 常量类
 *
 * @author Succy
 * @date 2017-02-19 22:15
 **/

public class Constant {
    private Constant() {
    }

    public static final String ROOT_KEY = "rpc";

    /**
     * 通过内部类定义响应码
     */
    public static final class RespCode {
        public static final int OK = 0;
        public static final int ERROR = -1;
    }


    /**
     * 通过内部类定义响应消息文本
     */
    public static final class RespMsg {
        public static final String getMsg(int respCode) {
            switch (respCode) {
                case RespCode.OK:
                    return "OK";
                case RespCode.ERROR:
                    return "Error";
                default:
                    return "";
            }
        }
    }

    /**
     * 提供配置文件相关的常量
     */
    public static final class Prop {
        public static final String CONF_FILE = "config.properties";
        public static final String BASE_SCAN_PACKAGE = "rpc.base_scan_package";
        public static final String REDIS_HOST = "redis.host";
        public static final String REDIS_PORT = "redis.port";
        public static final String REDIS_TIMEOUT = "redis.timeout";
        public static final String REDIS_POOL_MAXACTIVE = "redis.pool.maxActive";
        public static final String REDIS_POOL_MAXIDLE = "redis.pool.maxIdle";
        public static final String REDIS_POOL_MINIDLE = "redis.pool.minIdle";
        public static final String REDIS_POOL_MAXWAIT = "redis.pool.maxWait";
        public static final String REDIS_POOL_TESTONBORROW = "redis.pool.testOnBorrow";
        public static final String REDIS_POOL_TESTONRETURN = "redis.pool.testOnReturn";

    }
}
