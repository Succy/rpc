package cn.succy.rpc.comm.codec;

import cn.succy.rpc.comm.util.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 使用基于protobuf的protostuff对消息解码的解码器
 * 在构造该编码器的时候，指定要解码成的javabean的Class类型
 *
 * @author Succy
 * @date 2017/2/20 19:39
 */
public class ProtoDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;

    public ProtoDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 在编码的时候，会先将数据的长度写入，长度是int类型，占4个字节
        if (byteBuf.readableBytes() < 4) {

            // 读取到的数据如果<4，说明该包不完整或为空包
            return;
        }
        // 先mark一下，如果取出数据体的可读长度不等于获取到的长度的话，回滚到之前mark的位置，不对该数据做解码工作
        byteBuf.markReaderIndex();
        // 将传输的数据的长度获取
        int len = byteBuf.readInt();
        if (byteBuf.readableBytes() != len) {
            // 回滚，交给下一个解码器处理
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[len];
        byteBuf.readBytes(data);

        list.add(SerializableUtils.deserialize(data, clazz));
    }
}
