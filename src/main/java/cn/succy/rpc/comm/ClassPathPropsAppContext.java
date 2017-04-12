package cn.succy.rpc.comm;

import cn.succy.rpc.comm.kit.BeanKit;
import cn.succy.rpc.comm.kit.ClassKit;
import cn.succy.rpc.comm.kit.IocKit;
import cn.succy.rpc.comm.kit.PropsKit;
import cn.succy.rpc.comm.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * ioc容器的入口，类似Spring的ClasspathXmlApplicationContext
 *
 * @author Succy
 * @date 2017/4/11 13:59
 */
public class ClassPathPropsAppContext {
    private String propsFile;

    /**
     * 先把类都加载起来
     */
    static {
        Class<?>[] classList = {
                ClassKit.class, BeanKit.class, IocKit.class
        };

        for (Class<?> clazz : classList) {
            ClassUtils.loadClass(clazz.getName(), true);
        }
    }

    public ClassPathPropsAppContext() {
        init();
    }

    public ClassPathPropsAppContext(String propsFile) {
        this.propsFile = propsFile;
        init();
    }

    private void init() {
        PropsKit.use(this.propsFile);
    }

    /**
     * 通过Bean的类型获取Bean,和Spring一致
     * @param clazz Bean的类型
     * @return bean
     */
    public<T> T getBean(Class<T> clazz) {
        return BeanKit.getBean(clazz);
    }

    /**
     * 根据指定的注解获取在IOC容器中所有添加该注解的bean
     * @param clazz 指定的注解
     * @return bean的映射集合
     */
    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return BeanKit.getBeansMapWithAnnotation(clazz);
    }

}
