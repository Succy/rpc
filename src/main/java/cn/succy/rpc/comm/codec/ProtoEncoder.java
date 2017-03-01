package cn.succy.rpc.comm.codec;

import cn.succy.rpc.comm.util.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 使用基于protobuf的protostuff对消息编码的编码器
 * 在构造该编码器的时候，指定要编码的javabean的Class类型
 *
 * @author Succy
 * @date 2017/2/20 19:28
 */
public class ProtoEncoder extends MessageToByteEncoder {

    private Class<?> clazz;

    public ProtoEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf byteBuf) throws Exception {
        // 只对属于clazz的类型的实例进行编码
        if (clazz.isInstance(obj)) {
            long start = System.currentTimeMillis();
            byte[] data = SerializableUtils.serialize(obj);
            long end = System.currentTimeMillis();
            System.out.println("serialize times: " + (end - start) + " ms");
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
