/*
 * 文件名：TaskRunnable.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TaskRunnable.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月3日
 * 修改内容：新增
 */
package java1.util.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimerTask;

/**
 * 
 * @author zxiaofan
 */
public class TaskRunnable extends TimerTask {
    /**
     * 添加字段注释.
     */
    private String name;

    /**
     * 模拟执行时间.
     */
    private long executeTime = 0;

    /**
     * 多少次之后线程执行时间变为1s.
     */
    private Integer orignMax;

    /**
     * 构造函数.
     * 
     */
    public TaskRunnable() {
        super();
    }

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param executeTime
     *            executeTime
     */
    public TaskRunnable(String name, long executeTime) {
        super();
        this.name = name;
        this.executeTime = executeTime;
    }

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param executeTime
     *            原始执行时间
     * @param orignMax
     *            原始执行时间最大执行次数
     */
    public TaskRunnable(String name, long executeTime, Integer orignMax) {
        super();
        this.name = name;
        this.executeTime = executeTime;
        this.orignMax = orignMax;
    }

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     */
    public TaskRunnable(String name) {
        super();
        this.name = name;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        if (null != name && "null".equals(name)) {
            String npe = "模拟NPE异常，测试时不捕获此异常";
            System.out.println(npe);
            throw new NullPointerException(npe);
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.S");
            String startTime = format.format(new Date());
            if (null != orignMax && orignMax > 0) {
                orignMax--;
            }
            if (Objects.equals(0, orignMax)) { // 模拟多次任务时间间隔变化
                Thread.sleep(1000L);
            } else {
                Thread.sleep(executeTime);
            }
            System.err.println("[" + name + "]:run_" + startTime + ",end_" + format.format(new Date()));
        } catch (Exception e) { // TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行
            e.printStackTrace();
        }
    }

}
