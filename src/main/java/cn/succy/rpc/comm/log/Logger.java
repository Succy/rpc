package cn.succy.rpc.comm.log;

/**
 * 日志适配器接口，为了做日志的统一
 */
public interface Logger {
     void info(String msg);

     void info(Throwable ex);

     void info(String msg, Throwable ex);

     void info(String msg, Object... params);

     void debug(String msg, Throwable ex);

     void debug(String msg);

     void debug(Throwable ex);

     void debug(String msg, Object... params);

     void error(String msg, Throwable ex);

     void error(String msg);

     void error(Throwable ex);

     void error(String msg, Object... params);

     void warn(String msg);

     void warn(Throwable ex);

     void warn(String msg, Throwable ex);

     void warn(String msg, Object... params);
}
