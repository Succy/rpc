package cn.succy.rpc.test.client;

import cn.succy.rpc.client.BioRpcClient;
import cn.succy.rpc.test.api.HelloService;

import java.util.concurrent.CountDownLatch;

/**
 * @author Succy
 * @date 2017/2/28 19:53
 */
public class BioClient {

    private static CountDownLatch latch;
    public static void main(String[] args) {
        final Object obj = new Object();
        latch = new CountDownLatch(10);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (obj) {
                        long start = System.currentTimeMillis();
                        BioRpcClient client = new BioRpcClient();

                        HelloService service = client.getClientProxy(HelloService.class, "hello1");
                        String msg = service.sayHello("我是中国人！！！！");
                        System.out.println("msg: " + msg);
                        long end = System.currentTimeMillis();
                        long diffTime = end - start;
                        System.out.println("times: " + diffTime + " ms");
                        latch.countDown();
                    }
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("total times: " + (end - start) + " ms");
    }
}
