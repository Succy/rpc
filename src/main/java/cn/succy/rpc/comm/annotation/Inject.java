package cn.succy.rpc.comm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 依赖注入，类似Spring的@AutoWired
 * @author Succy
 * @date 2017/4/10 16:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
