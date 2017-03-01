package cn.succy.rpc.test.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Succy
 * @date 2017/2/27 9:30
 */
public class NioRpcClient {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        send("192.168.1.101", 9856);
        long end = System.currentTimeMillis();
        System.out.println("times: " + (end - start) + " ms");
    }

    private static void send(final String host, final int port) {
        SocketChannel channel = null;
        Selector selector = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            // connect
            channel.connect(new InetSocketAddress(host, port));
            // open selector
            selector = Selector.open();
            // register
            channel.register(selector, SelectionKey.OP_CONNECT);
            boolean isOver = false;
            while (!isOver) {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isConnectable()) {
                        if (channel.isConnectionPending()) {
                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_READ);
                                ByteBuffer buf = ByteBuffer.allocate(128);
                                buf.put("Hello, world$_".getBytes());
                                buf.flip();
                                while (buf.hasRemaining()) {
                                    channel.write(buf);
                                }
                            } else {
                                key.cancel();
                            }
                        }
                    }
                    // readAble
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        channel.read(buffer);
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);

                        String msg = new String(data, "UTF-8");
                        if (msg.endsWith("$_")) {
                            System.out.println("echo msg " + msg);
                            isOver = true;
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
