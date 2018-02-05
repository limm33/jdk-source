/*
 * 文件名：MyRecursiveTask.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： MyRecursiveTask.java
 * 修改人：yunhai
 * 修改时间：2015年12月9日
 * 修改内容：新增
 */
package java1.util.concurrent.ForkJoin;

import java.util.concurrent.RecursiveTask;

/**
 * RecursiveTask任务队列。
 * 
 * @author yunhai
 */
public class MyRecursiveTask extends RecursiveTask { // RecursiveTask表示有结果的任务
    private int start;

    private int end;

    public MyRecursiveTask(int start, int end) { // 任务可增加更多参数适应复杂情况
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    protected Object compute() {
        int result = 0;
        int resultLeft;
        int resultRight;
        if (end - start <= Constant.GROUP_THRESHOLD) {
            for (int i = start; i <= end; i++) {
                result += i; // 可自定义execute方法执行复杂任务
                JustSleep(); // 由于Java不支持多继承，无法继承模板方法，只能将休眠方法再次写一份。
                // execute(i);
            }
        } else {
            int middle = (start + end) / 2;
            MyRecursiveTask left = new MyRecursiveTask(start, middle);
            MyRecursiveTask right = new MyRecursiveTask(middle + 1, end);
            left.fork();
            right.fork();
            resultLeft = (int) left.join();
            resultRight = (int) right.join();
            result = resultLeft + resultRight;
        }
        return result;
    }

    private void execute(int i) {
        // TODO Auto-generated method stub

    }

    /**
     * 让线程休眠一下3ms，更加符合实际情况.
     * 
     */
    public void JustSleep() {
        AbstractTemplate abstractTemplate = new AbstractTemplate() {
        };
        abstractTemplate.JustSleep(); // 不建议如此调用【Abstract方法】，还是建议使用下面的方式
        // try {
        // Thread.sleep(Constant.INTERVAL_OF_SLEEP_MILLISECONDS);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
    }
}
