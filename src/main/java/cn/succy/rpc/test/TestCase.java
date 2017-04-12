package cn.succy.rpc.test;

import cn.succy.rpc.comm.ClassPathPropsAppContext;
import cn.succy.rpc.comm.annotation.Component;
import org.junit.Test;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Succy
 * @date 2017/4/11 14:24
 */
public class TestCase {
    @Test
    public void testBeanKit() {
        ClassPathPropsAppContext ctx = new ClassPathPropsAppContext();
        ComponentTest2 bean = ctx.getBean(ComponentTest2.class);
        Map<Class<?>, Object> beans = ctx.getBeansWithAnnotation(Component.class);
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key=" + key + "  value=" + value);
        }
    }

    @Test
    public void testReg() {
        String keys = ".*TXT\\s\"([^\"]+).*\"";
        String str = "verify.tszgg.com.\t3600\tIN\tTXT\t\"98373624358891368b3d04f920a93417\"";
        String mailStr = "succy.com.\t\t3600\tIN\tTXT\t\"v=spf1 ip6:fd92:59f3:510e::/48 -all\"";
        Pattern pattern = Pattern.compile(keys);
        Matcher matcher = pattern.matcher(mailStr);
        if (matcher.matches()) {
            System.out.println("matcher");
            String group = matcher.group(1);
            System.out.println(group);
        }
    }

}
