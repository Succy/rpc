package cn.succy.rpc.comm.util;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件加载工具类
 * @author Succy
 * @date 2017/4/10 17:09
 */
public final class PropsUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropsUtils.class);

    private PropsUtils() {
        throw new AssertionError("util class can not be instance");
    }

    /**
     * 加载指定的配置文件
     *
     * @param propsFileName 指定的配置文件名
     * @return 加载后的Properties对象
     */
    public static Properties loadProps(String propsFileName) {
        Properties props = null;
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propsFileName);
            if (null == in) {
                throw new FileNotFoundException(String.format("can not found props file: %s", propsFileName));
            }
            props = new Properties();
            props.load(in);

        } catch (IOException e) {
            logger.error("load props file error", e);
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }


    /**
     * 获取字符类型的字段值
     *
     * @param props        要从哪个props里边获取
     * @param key          要获取的字段key
     * @param defaultValue 默认值
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回默认值
     */
    public static String getString(Properties props, String key, String defaultValue) {
        String value = defaultValue;
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取字符类型的字段值
     *
     * @param props 要从哪个props里边获取
     * @param key   要获取的字段key
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回""
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取整型的字段值
     *
     * @param props        要从哪个props里边获取
     * @param key          要获取的字段key
     * @param defaultValue 默认值
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回默认值
     */
    public static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;
        if (props.containsKey(key)) {
            String strValue = props.getProperty(key);
            if (!StringUtils.isEmpty(strValue)) {
                try {
                    // 有可能获取过来的根本不是有个数字的字符串，会抛异常，捕获
                    value = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    /**
     * 获取整型的字段值
     *
     * @param props 要从哪个props里边获取
     * @param key   要获取的字段key
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回0
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    /**
     * 获取布尔型的字段值
     *
     * @param props        要从哪个props里边获取
     * @param key          要获取的字段key
     * @param defaultValue 默认值
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回默认值
     */
    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (props.containsKey(key)) {
            String strValue = props.getProperty(key);
            if (!StringUtils.isEmpty(strValue)) {
                value = Boolean.parseBoolean(strValue);
            }
        }

        return value;
    }

    /**
     * 获取布尔型的字段值
     *
     * @param props 要从哪个props里边获取
     * @param key   要获取的字段key
     * @return 当获取得到对应key的值则返回对应key的值，当获取不到的时候返回false
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }
}
