/*
 * 文件名：Constant.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Constant.java
 * 修改人：yunhai
 * 修改时间：2015年12月9日
 * 修改内容：新增
 */
package java1.util.concurrent.ForkJoin;

/**
 * 常量.
 * 
 * @author yunhai
 */
public class Constant {
    /**
     * 线程分组阈值.
     */
    public static final int GROUP_THRESHOLD = 2;

    /**
     * 多线程CPU倍数.
     */
    public static final int MULTIPLE_FORKJOIN_PROCESSORS = 3;

    /**
     * 睡眠时间（ms）.
     */
    public static final int INTERVAL_OF_SLEEP_MILLISECONDS = 9;
}
