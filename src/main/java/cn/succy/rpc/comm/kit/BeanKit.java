package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.util.ReflectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Succy
 * @date 2017/4/11 9:54
 */
public final class BeanKit {
    /**
     * 将Bean类和Bean的实例对象映射保存起来
     */
    private static final Map<Class<?>, Object> CLASS_BEAN_MAP = new ConcurrentHashMap<>();
    static {
        Set<Class<?>> beanClassSet = ClassKit.getAllBeanClassSet();
        for (Class<?> clazz : beanClassSet) {
            Object obj = ReflectionUtils.newInstance(clazz);
            CLASS_BEAN_MAP.put(clazz, obj);
        }
    }

    /**
     * 获取Class-Bean的映射集合
     */
    public static Map<Class<?>, Object> getBeanClassMap() {
        return CLASS_BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!CLASS_BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("Can't get class error");
        }

        return (T) CLASS_BEAN_MAP.get(clazz);
    }
}
