/*
 * 文件名：DrawThread.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： DrawThread.java
 * 修改人：yunhai
 * 修改时间：2015年11月17日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study.DrawCash;

/**
 * 同步方法就是synchronized关键字来修饰某个方法。
 * 
 * 对于同步方法而言，无需显示指定同步监视器，同步方法的同步监视器就是this，也就是该对象本身.
 * 
 * @author yunhai
 */
public class DrawThread_Method extends Thread {
    // 模拟用户账户
    private Account_Method account;

    // 当前取钱线程所希望取的钱数
    private double drawAmount;

    public DrawThread_Method(String name, Account_Method account, double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    // 当多条线程修改同一个共享数据时，将涉及到数据安全问题。
    public void run() {
        account.drawCash(drawAmount);
    }
}
