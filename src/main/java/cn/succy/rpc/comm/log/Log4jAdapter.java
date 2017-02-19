package cn.succy.rpc.comm.log;

import org.apache.log4j.PropertyConfigurator;

import java.io.InputStream;

/**
 * Log4j的适配器
 *
 * @author Succy
 * @date 2017-02-12 16:24
 **/

public class Log4jAdapter extends AbstractLoggerAdapter {
    private org.apache.log4j.Logger logger;

    public Log4jAdapter(Class clazz, InputStream in) {
        if (in == null ) {
            throw new NullPointerException("The Log4j config file is not found");
        }
        PropertyConfigurator.configure(in);
        logger = org.apache.log4j.Logger.getLogger(clazz != null ? clazz.getName() : " ");
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(Throwable ex) {
        logger.info(null, ex);
    }

    @Override
    public void info(String msg, Throwable ex) {
        logger.info(msg, ex);
    }

    @Override
    public void info(String msg, Object... params) {
        logger.info(String.format(msg, params));
    }

    @Override
    public void debug(String msg, Throwable ex) {
        logger.debug(msg, ex);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(Throwable ex) {
        logger.debug(null, ex);
    }

    @Override
    public void debug(String msg, Object... params) {
        logger.debug(String.format(msg, params));
    }

    @Override
    public void error(String msg, Throwable ex) {
        logger.error(msg, ex);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(Throwable ex) {
        logger.error(null, ex);
    }

    @Override
    public void error(String msg, Object... params) {
        logger.error(String.format(msg, params));
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(Throwable ex) {
        logger.warn(null, ex);
    }

    @Override
    public void warn(String msg, Throwable ex) {
        logger.warn(msg, ex);
    }

    @Override
    public void warn(String msg, Object... params) {
        logger.warn(String.format(msg, params));
    }
}
