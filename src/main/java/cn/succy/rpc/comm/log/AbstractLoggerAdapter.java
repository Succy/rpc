package cn.succy.rpc.comm.log;

/**
 * 抽象Logger适配器
 *
 * @author Succy
 * @date 2017-02-12 16:42
 **/

public abstract class AbstractLoggerAdapter implements Logger{
    @Override
    public void info(String msg) {

    }

    @Override
    public void info(Throwable ex) {

    }

    @Override
    public void info(String msg, Throwable ex) {

    }

    @Override
    public void info(String msg, Object... params) {

    }

    @Override
    public void debug(String msg, Throwable ex) {

    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(Throwable ex) {

    }

    @Override
    public void debug(String msg, Object... params) {

    }

    @Override
    public void error(String msg, Throwable ex) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(Throwable ex) {

    }

    @Override
    public void error(String msg, Object... params) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void warn(Throwable ex) {

    }

    @Override
    public void warn(String msg, Throwable ex) {

    }

    @Override
    public void warn(String msg, Object... params) {

    }
}
