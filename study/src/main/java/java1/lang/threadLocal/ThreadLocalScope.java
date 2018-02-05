/*
 * 文件名：ThreadLocalScope.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ThreadLocalScope.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月5日
 * 修改内容：新增
 */
package java1.lang.threadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

/**
 * ThreadLocal的作用域.
 * 
 * 顾名思义，每个线程都拥有单独的ThreadLocal，即便将上层的ThreadLocal实例传入子线程A，子线程A也将拥有新的ThreadLocal。
 * 
 * 【待续】newFixedThreadPool(nThreads)下提交的线程MyRunnable(String name, ThreadLocal<String> local)数量超过nThreads，
 * 
 * local拥有的AtomicInteger将重新初始化，即便AtomicInteger是全局static（其他类的public）也将重新初始化。
 * 
 * 【Note】SimpleDateFormat线程不安全，Java7及以下建议使用线程安全的工具类（{未使用Spring等方式实现单例时可}每个类new一个，但绝不能加static，不能传入多线程使用），
 * 
 * 【如果使用了Spring注入】由于Spring导致类单例，So即便每个类new一个非static的SimpleDateFormat，也是线程不安全的（即便非static的基本类型<如int>也只会初始化一次）。
 * 
 * 此处不包含Spring注入时的测试代码。
 * 
 * @author zxiaofan
 */
public class ThreadLocalScope {
    /**
     * Integer.
     */
    static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * ThreadLocal.
     */
    static ThreadLocal<String> local = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("HHmmssS");
            // return ThreadLocal_Study.atomicInteger.getAndIncrement() + ":" + format.format(new Date());
            return atomicInteger.getAndIncrement() + ":" + format.format(new Date());
        };
    };

    /**
     * 多线程测试ThreadLocal.
     * 
     * @param args
     *            args
     */
    @Test
    public static void main(String[] args) {
        int nThreads = 4;
        System.out.println(local.get()); // 0:103128921
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        // MyRunnable执行顺序不定，So可能出现name2:1:XXXXX
        service.submit(new MyRunnable("name1", local)); // name1_age1_statics1_1:103128926
        sleep(10); // 休眠10ms，避免多线程时毫秒精度不够
        service.submit(new MyRunnable("name2", local)); // name2_age1_statics11_2:103128936
        sleep(10);
        service.submit(new MyRunnable("name3", local)); // name3_age1_statics111_3:103128946
        sleep(2000);
        service.submit(new MyRunnable2("Hi-4", local)); // Hi-4_age2_statics2_4:103130949
        sleep(10);
        // 【Note】当nThreads=4时，按序第5次执行的Runnable打印出的AtomicInteger不在是递增后的5而是重新初始化后的1
        // 【待续】只有深入线程池底层才能解释其原因了
        service.submit(new MyRunnable2("Hi-5", local)); // Hi-5_age2_statics22_1:103128926
        sleep(2000);
        service.submit(new MyRunnable3("Boy-6", local)); // Boy-6_age1_statics3_2:103128936
        sleep(10);
        service.submit(new MyRunnable3("Boy-7", local)); // Boy-7_age1_statics33_3:103128946
        service.shutdown();
    }

    /**
     * sleep.
     * 
     * @param sleep
     *            sleep
     */
    private static void sleep(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * @author yunhai
 *
 */
class MyRunnable implements Runnable {
    /**
     * 添加字段注释.
     */
    private String name;

    /**
     * 添加字段注释.
     */
    private String age = "1";

    /**
     * staticParam.
     */
    private static String staticParam = "s1";

    /**
     * staticParam.
     */
    private static int staticint = 1;

    /**
     * 添加字段注释.
     */
    private ThreadLocal<String> local;

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param local
     *            local
     */
    public MyRunnable(String name, ThreadLocal<String> local) {
        super();
        this.name = name;
        this.local = local;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        // age每次输出都相同（1），static每次输出都会拼接一个1【static变量只初始化一次，static的基本类型如int也只会初始化一次】
        System.err.println(name + "_" + "age" + age + "_" + "static" + staticParam + "_" + local.get());
        age += "1";
        staticParam += "1";
        System.err.println("staticint:" + staticint);
        staticint++;
    }

}

/**
 * @author yunhai
 *
 */
class MyRunnable2 implements Runnable {
    /**
     * 添加字段注释.
     */
    private String name;

    /**
     * 添加字段注释.
     */
    private String age = "2";

    /**
     * staticParam.
     */
    private static String staticParam = "s2";

    /**
     * 添加字段注释.
     */
    private ThreadLocal<String> local;

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param local
     *            local
     */
    public MyRunnable2(String name, ThreadLocal<String> local) {
        super();
        this.name = name;
        this.local = local;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        System.err.println(name + "_" + "age" + age + "_" + "static" + staticParam + "_" + local.get());
        age += "2";
        staticParam += "2";
    }

}

/**
 * @author yunhai
 *
 */
class MyRunnable3 implements Runnable {
    /**
     * 添加字段注释.
     */
    private String name;

    /**
     * 添加字段注释.
     */
    private String age = "1";

    /**
     * staticParam.
     */
    private static String staticParam = "s3";

    /**
     * 添加字段注释.
     */
    private ThreadLocal<String> local;

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param local
     *            local
     */
    public MyRunnable3(String name, ThreadLocal<String> local) {
        super();
        this.name = name;
        this.local = local;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        System.err.println(name + "_" + "age" + age + "_" + "static" + staticParam + "_" + local.get());
        age += "3";
        staticParam += "3";
    }

}