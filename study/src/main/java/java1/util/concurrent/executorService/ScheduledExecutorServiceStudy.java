/*
 * 文件名：ScheduledExecutorServiceStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ScheduledExecutorServiceStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月4日
 * 修改内容：新增
 */
package java1.util.concurrent.executorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import java1.util.Timer.TaskRunnable;

/**
 * 任务抛出异常，ScheduledExecutorService就会停止执行任务，且不会再周期地执行该任务了，So Catch该Catch的异常.
 * 
 * ScheduledExecutorService和Timer的区别：
 * 
 * 1、Timer内部仅一个线程， task2在task1执行完毕后才开始执行（task1、task2的任务按序执行）；ScheduledExecutorService内部为线程池，支持多任务并发；
 * 
 * 2、TimerTask抛出RuntimeException，所有Task均会停止；ScheduledExecutorService的task1抛异常，不影响task2的运行。
 * 
 * 尽可能使用ScheduledExecutorService(JDK1.5以后)替代Timer。
 * 
 * 任务异常，导致该线程异常终止，该线程后续定时任务无法执行，So定时任务请在最外层捕获异常。
 * 
 * @author zxiaofan
 */
public class ScheduledExecutorServiceStudy {
    /**
     * 线程池.
     */
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

    /**
     * 延迟.
     */
    private long delay = 1L;

    /**
     * 间隔.
     */
    private long period = 1;

    /**
     * 执行时间.
     */
    private long executeTime = 1000L;

    @Test
    public void testDefault() {
        pool.schedule(new TaskRunnable("delay 1s", 4 * executeTime), delay, TimeUnit.SECONDS); // 【执行一次】延迟delay执行
        Runnable runnable = new TaskRunnable("scheduleAtFixedRate", 4 * executeTime, 3);
        pool.scheduleAtFixedRate(runnable, 2 * delay, 3 * period, TimeUnit.SECONDS); // 【和timer.scheduleAtFixedRate类似，任务实际执行时间趋向于计划时间】任务待执行的时间从一开始就确定，但若下一任务的actualTime>=expectTime，则下一任务立即执行
        runnable = new TaskRunnable("scheduleWithFixedDelay", 4 * executeTime, 3);
        pool.scheduleWithFixedDelay(runnable, 2 * delay, 3 * delay, TimeUnit.SECONDS); // 时间间隔确定，下次任务开始执行时间=上次任务结束时间+delay
        sleep(100000);
    }

    @Test
    public void testException() {
        pool.schedule(new TaskRunnable("delay 1s", 4 * executeTime), delay, TimeUnit.SECONDS); // 【执行一次】延迟delay执行
        pool.schedule(new TaskRunnable("null"), delay, TimeUnit.SECONDS);
        Runnable runnable = new TaskRunnable("null", 4 * executeTime, 3);
        pool.scheduleWithFixedDelay(runnable, 2 * delay, 3 * delay, TimeUnit.SECONDS); // 任务异常，导致该线程异常终止，该线程后续定时任务无法执行，So定时任务请在最外层捕获异常
        sleep(100000);
    }

    /**
     * sleep，确保任务已执行.
     * 
     * @param sleep
     *            sleep
     */
    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
