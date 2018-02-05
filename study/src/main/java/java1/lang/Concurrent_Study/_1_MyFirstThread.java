/*
 * 文件名：_1_MyFirstThread.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： MyThread.java
 * 修改人：yunhai
 * 修改时间：2015年11月16日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study;


import org.junit.Test;

/**
 * 创建线程的两种方式。
 * 
 * 1、创建Thread子类的一个实例并重写run方法：
 * 
 * 2、创建类时实现Runnable接口。
 * 
 * 线程池可以有效的管理实现了Runnable接口的线程，如果线程池满了，新线程将排队等候执行，直到线程池空闲出来为止。
 * 
 * 而如果线程是通过实现Thread子类实现的，这将会复杂一些。由于Java只允许单继承，所以如果自定义类需要继承其他类，则只能选择实现Runnable接口。
 * 
 * 有时我们要同时融合实现Runnable接口和Thread子类两种方式。例如，实现了Thread子类的实例可以执行多个实现了Runnable接口的线程。一个典型的应用就是线程池。
 * 
 * @author yunhai
 */
public class _1_MyFirstThread {
    /**
     * 一旦线程启动后start方法就会立即返回，而不会等到MyThread重写的run方法执行完毕才返回。
     * 
     * 就好像run方法是在另外一个cpu上执行一样。当run方法执行后，将会打印出run的输出内容.
     * 
     */
    @Test
    public void byExtends() { // 继承Thread类
        System.out.println("主线程ID:" + Thread.currentThread().getId());
        MyThread myThread1 = new MyThread("Thread1");
        myThread1.start();
        MyThread myThread2 = new MyThread("Thread2");
        // 如果调用run方法，即相当于在主线程中执行run方法，跟普通的方法调用没有任何区别，此时并不会创建一个新的线程来执行定义的任务。
        myThread2.run(); // 先输出myThread2，后输出myThread1；说明新线程创建的过程不会阻塞主线程的后续执行.
        // myThread2.start();
    }

    /**
     * 这种方式必须将Runnable作为Thread类的参数，然后通过Thread的start方法来创建一个新线程来执行该子任务。
     * 
     * 如果直接调用Runnable的run方法的话，是不会创建新线程的，这根普通的方法调用没有任何区别。
     * 
     */
    @Test
    public void byRunnable() { // 实现Runnable接口
        System.out.println("子线程ID：" + Thread.currentThread().getId());
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
        Thread thread1 = new Thread(myRunnable, "thread-hello"); // 线程名
        thread1.start();
    }

    @Test
    public void byInnerClass() { // 亦可通过匿名内部类来继承Thread或者实现Runnable
        // Thread匿名类
        // 当新的线程的run方法执行以后，将会打印出字符串"线程ID：..."。
        Thread t1 = new Thread() {
            public void run() {
                System.out.println("线程ID：" + Thread.currentThread().getId());
            }
        };
        t1.start();
        // 实现Runnable接口的匿名类
        // Runnable myRunnable = new Runnable() {
        // public void run() {
        // System.out.println("线程ID：" + Thread.currentThread().getId());
        // }
        // };
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                System.out.println("线程ID：" + Thread.currentThread().getId());
            }
        });
        t2.start();
    }

    /**
     * Thread类是实现了Runnable接口的.
     *
     */
    public class MyThread extends Thread {
        private String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("name:" + name + ", 子线程ID:" + Thread.currentThread().getId());
        }
    }

    public class MyRunnable implements Runnable {

        public MyRunnable() {
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            System.out.println("主线程ID:" + Thread.currentThread().getId());
        }
    }
}
