package cn.succy.rpc.comm.log;

/**
 * 日志适配器接口，为了做日志的统一
 */
public interface Logger {
    public void info(String msg);

    public void info(Throwable ex);

    public void info(String msg, Throwable ex);

    public void info(String msg, Object... params);

    public void debug(String msg, Throwable ex);

    public void debug(String msg);

    public void debug(Throwable ex);

    public void debug(String msg, Object... params);

    public void error(String msg, Throwable ex);

    public void error(String msg);

    public void error(Throwable ex);

    public void error(String msg, Object... params);

    public void warn(String msg);

    public void warn(Throwable ex);

    public void warn(String msg, Throwable ex);

    public void warn(String msg, Object... params);
}
