/*
 * 文件名：KW_volatile.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： KW_volatile.java
 * 修改人：yunhai
 * 修改时间：2015年12月9日
 * 修改内容：新增
 */
package keyWord;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * volatile关键字保证了操作的可见性，但是volatile能保证对变量的操作是原子性吗？
 * 
 * volatile关键字能保证可见性没有错，可见性只能保证每次读取的是最新的值，但是volatile没办法保证对变量的操作的原子性。
 * 
 * 自增操作不具备原子性，它包括读取变量的原始值、进行加1操作、写入工作内存。
 * 
 * volatile【不能】保证对变量的操作是原子性.
 * 
 * 那么该如何实现我们想要的结果呢？synchronized、ReentrantLock、AtomicInteger.
 * 
 * 参考资料：http://www.cnblogs.com/dolphin0520/p/3920373.html
 * 
 * http://www.cnblogs.com/aigongsi/archive/2012/04/01/2429166.html#!comments【评论很有意思】
 * 
 * @author yunhai
 */
public class KW_volatile {
    final int count = 1000;

    final int circle = 50; // 检验结果的循环次数

    final static int timeSleep = 3;

    /**
     * 重入锁.http://ifeve.com/reentrantlock-and-fairness/
     */
    Lock lock = new ReentrantLock();

    public volatile int num = 0;

    public int num2 = 0;

    public AtomicInteger inc = new AtomicInteger();

    /**
     * 仅仅使用Volatile关键字。结果小于count.
     * 
     */
    @Test
    public void JustVolatile() {// nonAtomic
        for (int i = 0; i < count; i++) {
            new Thread() {
                @Override
                public void run() {
                    num++;
                };
            }.start();
        }
        System.out.println(num); // 可能先运行到 System.out.println();而还剩几个新线程在跑。
    }

    /**
     * Volatile+CountDownLatch，多次测验，部分结果为count，部分非常接近count.
     * 
     */
    @Test
    public void VolatileCountDownLatch() {// nonAtomic
        for (int ii = 0; ii < circle; ii++) { // 定义一个循环，看第几次时num！=count
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        num++;
                        countDownLatch.countDown();
                        KW_volatile.sleep();
                    };
                }.start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ii);
            assertEquals(count, num);
            num = 0;
        }
    }

    /**
     * 让结果符合预期的4种方法.
     * 
     */
    @Test
    public void testOk() {
        synInc();
        synObject();
        lockInc();
        AtomicIntegerInc();
    }

    /**
     * AtomicInteger，一个提供原子操作的Integer的类。在Java语言中，++i和i++操作并不是线程安全的。
     * 
     * 而AtomicInteger则通过一种线程安全的加减操作接口。
     * 
     */
    private void AtomicIntegerInc() {
        for (int ii = 0; ii < circle; ii++) { // 定义一个循环，看第几次时num！=count
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        incAtomicInteger();
                        countDownLatch.countDown();
                    };
                }.start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(count, inc.intValue());
            System.out.println(ii);
            inc = new AtomicInteger(); // 重置inc
        }
    }

    /**
     * 自加操作-加锁lock.结果符合预期
     * 
     */
    private void lockInc() {
        for (int ii = 0; ii < circle; ii++) { // 定义一个循环，看第几次时num！=count
            ExecutorService service = Executors.newFixedThreadPool(count);
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                Runnable runable = new Runnable() {
                    @Override
                    public void run() {
                        incLock();
                        countDownLatch.countDown(); // countDownLatch结果不理想,将其放在循环体内就OK了。
                    };
                };
                service.execute(runable);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.shutdown();
            assertEquals(count, num2);
            System.out.println(ii);
            num2 = 0; // 重置num2
        }
    }

    /**
     * 将自加操作synchronized.
     * 
     */
    private void synInc() {
        for (int ii = 0; ii < circle; ii++) { // 定义一个循环，看第几次时num！=count
            final KW_volatile kwVolatile = new KW_volatile();
            for (int i = 0; i < count; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        kwVolatile.increase();
                    };
                }.start();
            }
            while (Thread.activeCount() > 2) { // 保证前面的线程都执行完(>2:main+ReaderThread线程)
                Thread.yield();
            }
            assertEquals(count, kwVolatile.num2);
            System.out.println(ii);
        }
    }

    /**
     * 将Object操作synchronized.
     * 
     */
    private void synObject() {
        for (int ii = 0; ii < circle; ii++) { // 定义一个循环，看第几次时num！=count
            Object object = new Object();
            for (int i = 0; i < count; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        synchronized (object) {
                            num2++;
                        }
                    };
                }.start();
            }
            while (Thread.activeCount() > 2) { // 保证前面的线程都执行完(>2:main+ReaderThread线程)
                Thread.yield();
            }
            assertEquals(count, num2);
            System.out.println(ii);
            num2 = 0;
        }
    }

    public synchronized void increase() {
        num2++;
    }

    public void incLock() {
        lock.lock();
        try {
            num2++;
        } finally {
            lock.unlock();
        }
    }

    protected void incAtomicInteger() {
        inc.incrementAndGet();
    }

    private static void sleep() {
        try {
            Thread.sleep(timeSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}