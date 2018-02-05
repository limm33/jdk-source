
package java1.lang.Concurrent_Study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import other.StopWatch;

/**
 * CAS保证共享变量的原子操作
 * 
 * 仅做学习使用，实际开发使用AtomicInteger
 * 
 * CAS的ABA问题，加序列号（1A，2B，3C）
 * 
 * CAS保证多个变量的原子性: 1、将变量拼接；2、强多个变量放入一个对象里，保证该对象的原子性（推荐）
 * 
 * @author zxiaofan
 */
public class CAS_Atom {
    static Integer index = 0;

    static Integer indexLock = 0;

    static String param = "A";

    static SafeCounter safeCounter = new SafeCounter(100);

    ExecutorService service1 = Executors.newFixedThreadPool(30);

    ExecutorService service2 = Executors.newFixedThreadPool(30);

    int n = 100000;

    /**
     * synchronized线程安全计数器.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testLock() throws InterruptedException {
        System.out.println("普通加锁...start...");
        System.out.println(param + "-ascii:" + (int) param.charAt(0));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < n; i++) {
            Runnable runnable = new RunableInt();
            service1.submit(runnable);
        }
        service1.shutdown();
        while (!service1.isTerminated()) {
            Thread.sleep(1000);
        }
        stopWatch.stop();
        for (int i = 0; i < 100; i++) {
            Runnable runnable = new RunableChar();
            service2.submit(runnable);
        }
        service2.shutdown();
        while (!service2.isTerminated()) {
            Thread.sleep(1000);
        }
        System.out.println("index:" + index);
        System.out.println(stopWatch.getTime());
        System.out.println(param + "-ascii:" + (int) param.charAt(0));
        System.out.println("普通加锁...End...");
    }

    /**
     * CAS线程安全计数器.
     * 
     * 单纯int累加比普通加锁略快，差别不明显。
     * 
     * @throws InterruptedException
     */
    @Test
    public void testCAS() throws InterruptedException {
        System.out.println("CAS...start...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < n; i++) {
            Runnable runnable = new RunableCASInt();
            service1.submit(runnable);
        }
        service1.shutdown();
        while (!service1.isTerminated()) {
            Thread.sleep(1000);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTime());
        System.out.println("safeCounter:" + safeCounter.getValue());
        System.out.println("CAS...End...");
    }
}

class RunableInt implements Runnable {

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        synchronized (CAS_Atom.indexLock) {
            // i++,基于auto casting,等同于i = new Integer(i + 1)
            // 故synchronized对有自加操作的Integer无效，对一个全新的Integer变量indexLock加锁即可
            CAS_Atom.index++;
        }
    }
}

class RunableChar implements Runnable {

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        synchronized (CAS_Atom.param) {
            CAS_Atom.param = String.valueOf(++CAS_Atom.param.toCharArray()[0]);
        }
    }
}

class RunableCASInt implements Runnable {

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        CAS_Atom.safeCounter.increase();
    }
}

/**
 * 线程安全的计数器
 *
 */
class SafeCounter {
    int value; // 初始值

    public SafeCounter() {
        super();
    }

    public SafeCounter(int original) {
        super();
        this.value = original;
    }

    public int getValue() {
        return value;
    }

    public int increase() {
        int v;
        do {
            v = value;
        } while (v != casIncrease(v, v + 1));
        return v + 1;
    }

    // 模拟CAS实现add
    private synchronized int casIncrease(int exceptValue, int newValue) {
        int oldValue = value;
        if (oldValue == exceptValue) {
            value = newValue;
        }
        return oldValue;
    }
}
