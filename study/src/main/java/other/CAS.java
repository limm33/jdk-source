/*
 * 文件名：CAS.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： CAS.java
 * 修改人：yunhai
 * 修改时间：2016年1月7日
 * 修改内容：新增
 */
package other;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

/**
 * CAS
 * 
 * @author yunhai
 */
public class CAS {
    // private static final Long circle = 1000_000_000L; // 22s-6s

    private static final Long circle = 1_000_000L; // 31ms-11ms

    private static final int threadNum = 5;

    private static final int activeCount = 2;

    static AtomicLong valAto = new AtomicLong(0);

    static long val = 0;

    /**
     * 此处，AtomicLong的效率是synchronized的3倍左右，数据量越大越明显。
     * 
     * AtomicLong.incrementAndGet的实现用了乐观锁技术，调用了 sun.misc.Unsafe 类库里面的 CAS算法，用CPU指令来实现【无锁自增】.
     */
    @Test
    public void testEfficiency() {
        synInc();
        AtomicLongInc();
    }

    /**
     * 多线程反而时间增加了.
     * 
     */
    @Test
    public void testThreadEfficiency() {
        StopWatch sw = new StopWatch();
        sw.start();
        for (int i = 0; i < threadNum; i++) {
            new Thread(new LoopSync()).start();
        }
        while (Thread.activeCount() > activeCount) { // 保证前面的线程都执行完(>2:main+ReaderThread线程)
            Thread.yield();
        }
        System.out.println("ThreadSync:" + sw.getTime());

        StopWatch sw1 = new StopWatch();
        sw1.start();
        for (int i = 0; i < threadNum; i++) {
            new Thread(new loopAto()).start();
        }
        while (Thread.activeCount() > activeCount) {
            Thread.yield();
        }
        System.out.println("ThreadAtomic:" + sw1.getTime());
    }

    private void synInc() {
        StopWatch sw = new StopWatch();
        sw.start();
        for (int ii = 0; ii < circle;) {
            synchronized (CAS.class) {
                ii++;
            }
        }
        System.out.println("synInc:" + sw.getTime());
    }

    private void AtomicLongInc() {
        StopWatch sw = new StopWatch();
        sw.start();
        for (AtomicLong ii = new AtomicLong(0L); ii.incrementAndGet() < circle;) {
        }
        System.out.println("AtomicInc:" + sw.getTime());
    }

    // 多线程
    private class LoopSync implements Runnable {
        Object object = new Object();

        public void run() {
            while (val < circle) {
                synchronized (object) {
                    val++;
                }
            }
        }
    }

    private class loopAto implements Runnable {
        public void run() {
            while (valAto.incrementAndGet() < circle) {

            }
        }
    }

}