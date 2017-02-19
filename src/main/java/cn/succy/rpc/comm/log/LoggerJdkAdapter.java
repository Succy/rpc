package cn.succy.rpc.comm.log;

import java.util.logging.Level;

/**
 * 适配jdk日志的适配器
 *
 * @author Succy
 * @date 2017-02-12 12:26
 **/

public class LoggerJdkAdapter extends AbstractLoggerAdapter {
    private java.util.logging.Logger logger;

    public LoggerJdkAdapter(Class clazz) {
        logger = java.util.logging.Logger.getLogger(clazz != null ? clazz.getName() : " ");
    }

    @Override
    public void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    @Override
    public void info(Throwable ex) {
        logger.log(Level.INFO, ex.getMessage(), ex);
    }

    @Override
    public void info(String msg, Throwable ex) {
        logger.log(Level.INFO, msg, ex);
    }

    @Override
    public void info(String msg, Object... params) {
        logger.info(String.format(msg, params));
    }

    @Override
    public void debug(String msg) {
        logger.log(Level.CONFIG, msg);
    }

    @Override
    public void debug(String msg, Throwable ex) {
        logger.log(Level.CONFIG, msg, ex);
    }

    @Override
    public void debug(Throwable ex) {
        logger.log(Level.CONFIG, ex.getMessage(), ex);
    }

    @Override
    public void debug(String msg, Object... params) {
        logger.config(String.format(msg, params));
    }

    @Override
    public void error(String msg, Throwable ex) {
        logger.log(Level.SEVERE, msg, ex);
    }

    @Override
    public void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    @Override
    public void error(Throwable ex) {
        logger.log(Level.SEVERE, ex.getMessage(), ex);
    }

    @Override
    public void error(String msg, Object... params) {
        logger.severe(String.format(msg, params));
    }

    @Override
    public void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    @Override
    public void warn(Throwable ex) {
        logger.log(Level.WARNING, ex.getMessage(), ex);
    }

    @Override
    public void warn(String msg, Throwable ex) {
        logger.log(Level.WARNING, msg, ex);
    }

    @Override
    public void warn(String msg, Object... params) {
        logger.warning(String.format(msg, params));
    }
}
