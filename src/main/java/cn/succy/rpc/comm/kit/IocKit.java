package cn.succy.rpc.comm.kit;

import cn.succy.rpc.comm.annotation.Inject;
import cn.succy.rpc.comm.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 用来实现依赖注入的助手类，当字段中使用@Inject注解标记时，从BeanKit中取出对应的Bean
 * 通过ReflectionUtils给设置进去
 *
 * @author Succy
 * @date 2017/4/11 10:22
 */
public final class IocKit {
    static {
        init();
    }

    /**
     * 从ClassMap中，遍历所有的entry，得到所有的Class对象，取出其里边标记有@Inject注解的字段，并取出ClassMap
     * 中对应字段Type的实例化对象，并且赋值给字段
     */
    private static void init() {
        Map<Class<?>, Object> beanClassMap = BeanKit.getBeanClassMap();
        if (beanClassMap != null && !beanClassMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : beanClassMap.entrySet()) {
                Class<?> classKey = entry.getKey();
                Object beanValue = entry.getValue();

                Field[] fields = classKey.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Inject.class)) {
                            // 获取标记了@Inject的字段(都是Bean类型)类型的Class
                            Class<?> fieldType = field.getType();
                            Object instance = BeanKit.getBean(fieldType);
                            if (null != instance) {
                                ReflectionUtils.setField(beanValue, field, instance);
                            }
                        }
                    }// end of for:fields
                }
            }// end of for:entry
        }
    }
}
