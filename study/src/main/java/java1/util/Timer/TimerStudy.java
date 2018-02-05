/*
 * 文件名：TimerStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TimerStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月3日
 * 修改内容：新增
 */
package java1.util.Timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;

/**
 * Timer.
 * 
 * Timer简单易用， 所有任务都由同一个线程调度，因此所有任务都是串行执行，同一时间只能有一个任务在执行，前一个任务的延迟或异常都将会影响到之后的任务。
 * 
 * TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行
 * 
 * 1、schedule()：fixed-delay间隔时间固定（上次任务执行完毕后，若executeTime>=period，立即执行下一次任务；若executeTime<period，延迟delay再执行下一次任务）
 * 
 * 2、scheduleAtFixedRate()：fixed-rate执行频率固定（任务队列中每个任务待执行的时间从一开始就确定，理论上任务执行间隔period）（任务实际执行时间趋向于计划时间）；
 * 
 * （实际执行时，若（个别任务executeTime>period导致）下一任务的actualTime>=expectTime，则下一任务立即执行，任务队列中的任务将依次延后执行，当后续任务的executeTime<period时，任务的实际执行时间会逐渐趋向于一开始就确定的待执行时间。）
 * 
 * @author zxiaofan
 */
public class TimerStudy {
    /**
     * 添加字段注释.
     */
    Timer timerDefault = new Timer();

    /**
     * 添加字段注释.
     */
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * 延迟.
     */
    private static long delay = 1000L;

    /**
     * 间隔.
     */
    private static long period = 1000L;

    /**
     * 执行时间单位间隔.
     */
    private static long executeTime = 1000L;

    /**
     * timerDefault.
     * 
     */
    @Test
    public void testDefault() {
        printNow();
        TimerTask task = new TaskRunnable("延迟指定时间执行任务...");
        // TimerTask task = new TaskRunnable("null");
        timerDefault.schedule(task, delay); // schedule(TimerTask task, long delay)：以当前时间为准，延迟delay毫秒后执行Task（仅执行一次）
        Date firstTime = null;
        try {
            firstTime = format.parse("2015-11-12 00:11:22.3");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sleep(1000);
        // TimerTask不能重用
        TimerTask task2 = new TaskRunnable("指定时间执行任务...");
        timerDefault.schedule(task2, firstTime); // schedule(TimerTask task, Date time)：在指定时间执行任务，如果时间已过期立即执行任务（仅执行一次）
        sleep(1000);
        //
        TimerTask task3 = new TaskRunnable("延迟delay，间隔period执行任务...");
        timerDefault.schedule(task3, delay, 2 * period);
        sleep(1000);
        //
        TimerTask task4 = new TaskRunnable("...从指定时间开始，间隔period执行任务...");
        timerDefault.schedule(task4, firstTime, 4 * period); // 若时间已过期立即执行任务
        sleep(100000);
    }

    /**
     * timerDefault.
     * 
     * 待确认.
     */
    @Test
    public void testScheduleAtFixedRate() {
        printNow();
        TimerTask task = new TaskRunnable("延迟delay，间隔period执行任务...scheduleAtFixedRate频率固定...", 4 * executeTime, 5);
        timerDefault.scheduleAtFixedRate(task, 2 * delay, 2 * period); // executeTime>period，设置executeTime最大执行次数后，即可发现scheduleAtFixedRate执行的奥秘
        // Date firstTime = null;
        // try {
        // firstTime = format.parse("2015-11-12 00:11:22.3");
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        // timerDefault.scheduleAtFixedRate(task, firstTime, 3000L); // 指定firstTime时间后，每次间隔period开始执行任务（firstTime过期则立即执行）
        TimerTask task2 = new TaskRunnable("延迟delay，间隔period执行任务...schedule间隔固定...", 4 * executeTime, 5);
        // timerDefault.schedule(task2, 2 * delay, 3 * period);
        sleep(100000);
    }

    /**
     * Timer守护线程.
     * 
     * Java 线程分为两类：用户线程（User Thread）和守护线程（Daemon Thread）
     * 
     * 守护线程的作用是为其他线程提供服务，譬如垃圾回收器（GC），只要当前 JVM 实例中还有非守护线程运行，则守护线程就会一直工作下去，直至所有非守护线程结束，守护线程随 JVM 一起结束。
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        boolean isDaemon = false; // 如果Timer设置为守护线程true，用户线程执行完毕后，timerDaemon的任务也会随之结束；反之若Timer非守护，则其会一直运行
        Timer timerDaemon = new Timer("TimerName", isDaemon);
        timerDaemon.schedule(new TaskRunnable("task", 3 * executeTime), delay, 2 * period);
        for (int i = 0; i < 10; i++) {
            sleep(executeTime); // 用户线程（运行10S即结束）
        }
        System.out.println("用户线程执行完毕");
    }

    /**
     * sleep，确保任务已执行.
     * 
     * @param sleep
     *            sleep
     */
    private static void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * printNow.
     * 
     */
    private void printNow() {
        System.out.println("now:" + format.format(new Date()));
    }
}
