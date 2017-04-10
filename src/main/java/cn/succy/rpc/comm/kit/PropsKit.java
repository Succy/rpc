package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.util.Constant;
import cn.succy.rpc.comm.util.PropsUtils;

import java.util.Properties;

/**
 * 操作配置文件的助手类
 * @author Succy
 * @date 2017/4/10 17:38
 */
public final class PropsKit {
    private static final Properties PROPS = PropsUtils.loadProps(Constant.Prop.CONF_FILE);

    public static String getBaseScanPackage() {
        return PropsUtils.getString(PROPS, Constant.Prop.BASE_SCAN_PACKAGE);
    }
}
