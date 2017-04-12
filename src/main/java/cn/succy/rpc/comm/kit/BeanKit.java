package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.util.ClassUtils;
import cn.succy.rpc.comm.util.ReflectionUtils;
import cn.succy.rpc.comm.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
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

    /**
     * 根据指定的注解获取在IOC容器中所有添加该注解的bean
     *
     * @param clazz 指定的注解
     * @return bean的映射集合
     */
    public static Map<Class<?>, Object> getBeansMapWithAnnotation(Class<? extends Annotation> clazz) {
        Map<Class<?>, Object> beanMap = new HashMap<>();
        for (Map.Entry<Class<?>, Object> entry : CLASS_BEAN_MAP.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            if (key.isAnnotationPresent(clazz)) {
                beanMap.put(key, value);
            }
        }
        return beanMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!CLASS_BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("Can't get class error");
        }

        return (T) CLASS_BEAN_MAP.get(clazz);
    }

    /**
     * 根据类名获取Bean
     * @param className 类名
     * @return 获取到的bean
     */
    public static Object getBean(String className) {
        Object bean = null;
        if (!StringUtils.isEmpty(className)) {
            Class<?> clazz = ClassUtils.loadClass(className, false);
            bean = ReflectionUtils.newInstance(clazz);

        }
        return bean;
    }
}
