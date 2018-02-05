/*
 * 文件名：ForkJoinPool_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ForkJoinPool_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月9日
 * 修改内容：新增
 */
package java1.util.concurrent.ForkJoin;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

/**
 * ForkJoin，工作窃取（work-stealing）算法
 * 
 * 本测试用例通过计算求1到n的和来演示ForkJoin，为了符合实际开发情况，每add一次均增加了休眠时间。
 * 
 * PS：如果不休眠，【ForkJoin】Time:9ms 【Common】Time:0ms，ForkJoin分而治之多耗费了几毫秒的时间，但这显示不符合实际开发情况，有兴趣的读者可自行尝试。
 * 
 * 
 * 【ForkJoin】 result=500500; Time:885ms 【Common】 result=500500; Time:9010ms
 * 
 * 由测试结果可以看出，ForkJoin框架消耗的时间约为单线程的十分之一，前段时间在公司做的一个实际项目用ForkJoin的效率也是单线程的10倍左右。
 * 
 * 相关资料：http://ifeve.com/talk-concurrency-forkjoin/
 * 
 * @author yunhai
 */
public class ForkJoinPool_Study extends AbstractTemplate {
    ForkJoinPool pool = new ForkJoinPool(Constant.MULTIPLE_FORKJOIN_PROCESSORS * Runtime.getRuntime().availableProcessors());

    int n = 1000;

    @Test
    public void Compare() {
        MyForkJoin();
        Common();
    }

    private void MyForkJoin() {
        Long timeStart = System.currentTimeMillis();
        int result = 0;
        MyRecursiveTask task = new MyRecursiveTask(0, n);
        result = (int) pool.invoke(task);
        System.out.println("【ForkJoin】  result=" + result + ";  Time:" + (System.currentTimeMillis() - timeStart) + "ms");
    }

    private void Common() {
        Long timeStart = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i <= n; i++) {
            result += i;
            JustSleep();
        }
        System.out.println("【Common】  result=" + result + ";  Time:" + (System.currentTimeMillis() - timeStart) + "ms");
    }
}
