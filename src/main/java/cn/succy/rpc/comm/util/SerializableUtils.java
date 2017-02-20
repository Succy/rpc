package cn.succy.rpc.comm.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用基于protobuf的protostuff对Javabean进行序列化成byte[] 和对byte[]反序列化成javabean的工具类
 *
 * @author Succy
 * @date 2017/2/20 17:49
 */
public class SerializableUtils {
    private SerializableUtils() {
    }

    // 生成schema比较耗时，缓存起来
    private static Map<Class<?>, Schema> cacheMap = new ConcurrentHashMap<>();

    /**
     * 对指定Java对象进行序列化
     *
     * @param obj 要序列化的Java对象
     * @param <T> 对象的泛型类型
     * @return 序列化后的字节数组，如果中间有异常，则向上抛出非法状态异常
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 将字节数组反序列化成指定类型的Java对象
     *
     * @param data  要反序列化的数据
     * @param clazz 反序列化成的对象类型
     * @param <T>   对象的泛型类型
     * @return 如果成功，返回反序列化后的对象，若出现异常，则向上抛出非法状态异常
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            Schema<T> schema = getSchema(clazz);
            T message = clazz.newInstance();
            ProtobufIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 生成schema
     *
     * @param clazz 要生成schema的Class对象
     * @param <T>   对象的泛型类型
     * @return 返回生成或者从缓存中取出的schema对象
     */
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        if (cacheMap.containsKey(clazz)) {
            return cacheMap.get(clazz);
        }
        Schema<T> schema = RuntimeSchema.createFrom(clazz);
        if (schema != null) {
            cacheMap.put(clazz, schema);
        }
        return schema;
    }
}
