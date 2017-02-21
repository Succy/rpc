package cn.succy.rpc.comm.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 日志工厂，用来获取日志类
 *
 * @author Succy
 * @date 2017-02-12 13:16
 **/

public class LoggerFactory {
    private static Logger logger;
    private static String mConfigFileName;

    private LoggerFactory() {
    }

    /**
     * 配置日志的配置文件
     *
     * @param configFileName
     */
    public static void configure(String configFileName) {
        mConfigFileName = configFileName;
    }

    public static Logger getLogger(Class clazz) {
        InputStream in = null;
        try {
            if (mConfigFileName == null || "".equals(mConfigFileName)) {
                mConfigFileName = "log4j.properties";
                in = ClassLoader.getSystemResourceAsStream(mConfigFileName);
            } else {
                    in = new FileInputStream(mConfigFileName);
            }
            if (in == null) {
                logger = new LoggerJdkAdapter(clazz);
            } else {
                logger = new Log4jAdapter(clazz, in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mConfigFileName = null;
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }
}
