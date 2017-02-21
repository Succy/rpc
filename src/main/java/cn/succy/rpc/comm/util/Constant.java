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
}
