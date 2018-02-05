/*
 * 文件名：AbstractTemplate.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： AbstractTemplate.java
 * 修改人：yunhai
 * 修改时间：2015年12月9日
 * 修改内容：新增
 */
package java1.util.concurrent.ForkJoin;

/**
 * 模板方法.
 * 
 * 由于Java不支持多继承，MyRecursiveTask无法继承模板方法，只能将休眠方法再次写一份。
 * 
 * 在这里写模板方法，只是为了提醒自己将公用模板抽象化。
 * 
 * @author yunhai
 */
public abstract class AbstractTemplate {
    /**
     * 让线程休眠一下3ms，更加符合实际情况.
     * 
     */
    public void JustSleep() {
        try {
            Thread.sleep(Constant.INTERVAL_OF_SLEEP_MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
