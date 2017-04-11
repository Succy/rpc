package cn.succy.rpc.comm.util;

import cn.succy.rpc.comm.log.Logger;
import cn.succy.rpc.comm.log.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作的工具类
 *
 * @author Succy
 * @date 2017/4/10 13:06
 */
public final class ClassUtils {
    private final static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private ClassUtils() {
        throw new AssertionError("utils class can not be instance");
    }

    /**
     * 获取当前线程的类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类，根据类名和是否初始化静态代码块，返回加载出的类
     *
     * @param className 指定要加载的类名
     * @param isInit    是否初始化类中的静态代码块
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInit) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInit, getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 获取指定包名下所有的类
     *
     * @param packageName 指定包名
     * @return 指定包名下所有类的集合
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            // 获取指定包名下所有的class文件或者jar文件
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            // 遍历所有的url
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    // 获取protocol的目的是区分开class文件和jar文件
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        // 获取子包或者class文件
                        String pkgPath = url.getPath().replaceAll("%20", "");
                        addClasses(classSet, pkgPath, packageName);
                    } else if ("jar".equals(protocol)) {
                        // 打开jar包，并取得jar包内部的class文件
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            // 取得JarFile对象
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                // 取出JarFile对象中所有的文件
                                Enumeration<JarEntry> entries = jarFile.entries();
                                while (entries.hasMoreElements()) {
                                    JarEntry jarEntry = entries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    // 取出JarFile里边的class文件
                                    if (jarEntryName.endsWith(".class")) {
                                        // 取得class文件的path，类似com/abc/Abc.class，并且去掉后面的.class，方便得到全类名
                                        String classPath = jarEntryName.substring(0, jarEntryName.lastIndexOf("."));
                                        String className = classPath.replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get classSet failure", e);
            throw new RuntimeException(e);
        }

        return classSet;
    }

    /**
     * 将一个包下所有的类添加到classSet
     *
     * @param classSet    要添加到的classSet
     * @param packagePath 包名对应的路径
     * @param packageName 包名
     */
    private static void addClasses(Set<Class<?>> classSet, String packagePath, String packageName) {
        // 过滤出子包或者.class文件
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // 去掉.class
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!StringUtils.isEmpty(className)) {
                    className = packageName + "." + className;
                    doAddClass(classSet, className);
                }
            } else {
                // 此时file对应的应该是子包
                if (!StringUtils.isEmpty(fileName)) {

                    String subClassPkgPath = fileName;
                    subClassPkgPath = packagePath + "/" + subClassPkgPath;
                    String subClassPkgName = fileName;
                    subClassPkgName = packageName + "." + subClassPkgName;

                    addClasses(classSet, subClassPkgPath, subClassPkgName);
                }
            }
        }
    }

    /**
     * 将指定的类名的Class对象获得，并添加到classSet集合中
     *
     * @param classSet  目标集合
     * @param className 要获取Class对象的类名
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }
}
