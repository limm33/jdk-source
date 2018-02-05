/*
 * 文件名：_2_Synchronized.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： _2_Synchronized.java
 * 修改人：yunhai
 * 修改时间：2015年11月17日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study;

/**
 * 详见例子：DrawCash
 * 
 * @author yunhai
 */
public class _2_Synchronized {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new _2_Synchronized().init();
    }

    /**
     * 要互斥，必须让锁子是同一把。两个线程都用的是同一个new出来的output，所以output就是同一个对象.
     * 
     */
    private void init() {// 此方法同时启动两个线程，去调用同一个方法的打印名字
        final Outputer outputer = new Outputer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("MaYun"); // name长一点，sleep短一点，效果才明显
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("ZhangWuJi");
                }

            }
        }).start();

    }

    /**
     * 当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内只能有一个线程得到执行。
     * 
     * 另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
     * 
     * @author yunhai
     *
     */
    static class Outputer {
        public void output(String name) {
            int len = name.length();
            synchronized (Outputer.class) { // 注释
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            } //
        }
    }
}
