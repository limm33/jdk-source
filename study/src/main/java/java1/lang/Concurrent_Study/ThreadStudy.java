package java1.lang.Concurrent_Study;

import org.junit.Test;

/**
 * 
 * @author zxiaofan
 */
public class ThreadStudy implements Runnable {
    private static int a = 0; // 必须用static声明为静态全局变量，被 static 修饰的方法和属性只属于类不属于类的任何对象

    @Test
    public void testJoin() throws InterruptedException {
        Runnable r = new ThreadStudy();
        Thread t = new Thread(r);
        t.start();
        t.join(); // join()/join(long millis)等待该线程终止(主线程等待子线程的终止),确保该线程执行完毕再继续往下执行
        // 注：join方法只对启动了(start)的线程有效
        System.out.println(a); // 有Join方法输出300，无join方法输出0
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        for (int k = 0; k < 300; k++) {
            a = a + 1;
        }
    }
}
