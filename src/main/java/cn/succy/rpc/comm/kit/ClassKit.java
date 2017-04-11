package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.annotation.Component;
import cn.succy.rpc.comm.annotation.RpcService;
import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;
import cn.succy.rpc.comm.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作的助手类
 *
 * @author Succy
 * @date 2017/4/10 16:46
 */
public final class ClassKit {

    private static final Logger logger = LoggerFactory.getLogger(ClassKit.class);

    // 基础包下所有的类的集合
    private static final Set<Class<?>> CLASS_SET;

    static {
        String baseScanPackage = PropsKit.getBaseScanPackage();
        CLASS_SET = ClassUtils.getClassSet(baseScanPackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取所有配置了@RpcService的类
     */
    public static Set<Class<?>> getRpcServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(RpcService.class)) {
                classSet.add(clazz);
            }
        }

        return classSet;
    }

    /**
     * 获取所有配置了@Component的类
     */
    public static Set<Class<?>> getComponentClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Component.class)) {
                classSet.add(clazz);
            }
        }

        return classSet;
    }

    /**
     * 为了日后拓展其他注解方便，提供一个返回所有的Bean的方法
     */
    public static Set<Class<?>> getAllBeanClassSet() {
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getRpcServiceClassSet());
        beanClassSet.addAll(getComponentClassSet());
        return beanClassSet;
    }
}
