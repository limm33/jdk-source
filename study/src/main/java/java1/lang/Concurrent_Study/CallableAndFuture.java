/*
 * 文件名：CallableAndFuture.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： CallableAndFuture.java
 * 修改人：yunhai
 * 修改时间：2015年11月23日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 启动一个线程不论使用Thread或者Runnable的时候，都是没有返回结果的。也就是说Thread和Runnable的run()方法必须没有返回值。
 * 
 * Callable和Future，一个产生结果，一个拿到结果。
 * 
 * @author yunhai
 */
public class CallableAndFuture {
    public static void main(String[] args) {
        // 单一线程的线程池
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<String> future = threadPool.submit(new Callable<String>() {
            public String call() throws Exception {
                Thread.sleep(2000);
                return "hello";
            };
        });
        System.out.println("等待结果...");
        try {
            System.out.println("拿到结果 " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}