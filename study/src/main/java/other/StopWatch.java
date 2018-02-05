/*
 * 文件名：StopWatch.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： StopWatch.java
 * 修改人：yunhai
 * 修改时间：2016年1月7日
 * 修改内容：新增
 */
package other;

/**
 * StopWatch的简单自我实现
 * 
 * 支持stopTime-startTime；支持currentTimeMillis-startTime
 * 
 * @author yunhai
 */
public class StopWatch {
    private long startTime = 0;

    private long stopTime = 0;

    private boolean running = false;

    public StopWatch() { // 支持创建实例及开始计时，调用start方法重新计时。
        super();
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    public String getTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return toString(elapsed);
    }

    public Long getLongTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }

    private String toString(long ms) {
        // long型毫秒正确转换
        long SSS = ms % 1000;
        long ss = (ms - SSS) / 1000 % 60;
        long mm = (ms - SSS) / 1000 / 60 % 60;
        long HH = (ms - SSS) / 1000 / 60 / 60 % 24;
        // long dd = (ms - SSS) / 1000 / 60 / 60 / 24;// 天数取余得考虑月份实际情况
        String result = HH + ":" + mm + ":" + ss + "." + SSS;
        return result;
    }
}
