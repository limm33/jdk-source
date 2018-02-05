/*
 * 文件名：CompletionService.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： CompletionService.java
 * 修改人：yunhai
 * 修改时间：2015年11月23日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletionService用于提交一组Callable任务，其take方法返回已完成的一个Callable任务对应的Future对象。
 * 
 * @author yunhai
 */
public class CompletionService_Study {
    public static void main(String[] args) {

        ExecutorService threadPool2 = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool2);
        for (int i = 1; i <= 5; i++) {
            final int seq = i;
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    // nextInt(int n) 方法用于获取一个伪随机，在0=<x<n，从此随机数生成器的序列中取出均匀分布的int值。
                    Thread.sleep(new Random().nextInt(5000));
                    return seq;
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
