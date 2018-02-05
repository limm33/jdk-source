/*
 * 文件名：ReentrantLockTest.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ReentrantLockTest.java
 * 修改人：yunhai
 * 修改时间：2015年12月10日
 * 修改内容：新增
 */
package java1.util.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * ReentrantLockTest可重入锁【未能深入理解，Mark一下】
 * 
 * http://ifeve.com/reentrantlock-and-fairness/
 * 
 * http://www.ibm.com/developerworks/cn/java/j-jtp10264/index.html
 * 
 * @author yunhai
 */
public class ReentrantLockTest {
    private static Lock fairLock = new ReentrantLock(true);

    private static Lock unfairLock = new ReentrantLock();

    /**
     * 公平锁.
     * 
     */
    @Test
    public void fair() {
        System.out.println("fair version");
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Job(fairLock));
            thread.setName("" + i);
            thread.start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unfair() {
        System.out.println("unfair version");
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Job(unfairLock));
            thread.setName("" + i);
            thread.start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Job implements Runnable {
        private Lock lock;

        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    System.out.println("Lock by:" + Thread.currentThread().getName());
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}