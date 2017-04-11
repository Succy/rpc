package cn.succy.rpc.comm.util;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类，用来使用反射创建出类的实例
 *
 * @author Succy
 * @date 2017/4/11 9:32
 */
public final class ReflectionUtils {
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 实例化指定的Class对象
     *
     * @param clazz 指定的Class对象
     * @return 实例化对象
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            logger.error("instance %s failure", e, clazz.getName());
            throw new RuntimeException(e);
        }

        return instance;
    }

    /**
     * 反射调用方法
     *
     * @param obj    要调用的方法的对象
     * @param method 要调用的方法
     * @param args   方法的参数
     * @return 方法执行后的结果
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            logger.error("invoke method: %s failure", e, method.getName());
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 给字段设置值
     *
     * @param obj   要设置值的字段所在的对象
     * @param field 要设置值的字段
     * @param args  字段的参数
     */
    public static void setField(Object obj, Field field, Object args) {
        try {
            field.setAccessible(true);
            field.set(obj, args);
        } catch (IllegalAccessException e) {
            logger.error("set field failure");
            throw new RuntimeException(e);
        }
    }
}
