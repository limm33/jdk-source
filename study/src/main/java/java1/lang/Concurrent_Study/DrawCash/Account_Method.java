/*
 * 文件名：Account.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Account.java
 * 修改人：yunhai
 * 修改时间：2015年11月17日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study.DrawCash;

/**
 * 同步方法drawCash.银行取钱账户类。
 * 
 * @author yunhai
 */
public class Account_Method {
    // 账号
    private String accountNo;

    // 余额
    private double balance;

    /**
     * 构造函数，设置账号和余额 .
     * 
     * @param accountNo
     * @param balance
     */
    public Account_Method(String accountNo, int balance) {
        this.accountNo = accountNo;
        this.balance = balance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    // public void setBalance(int balance) { // 账户余额不能随便修改，注释掉
    // this.balance = balance;
    // }

    /**
     * 提供一个线程安全的方法完成取钱操作.
     * 
     * 将取钱的方法重构到了Account类，并使用synchronized修饰该方法，使其成为同步方法。
     * 
     * 同步方法的同步监视器是this，因为对于同一account对象来说，任意时刻只能有一个线程获得对account对象的锁定，然后执行取钱的操作。这样就可以保证线程安全。
     * 
     * @param drawAmount
     *            取款金额.
     */
    public synchronized void drawCash(double drawAmount) {
        if (balance >= drawAmount) {
            System.out.println(Thread.currentThread().getName() + " 取钱成功，取款金额：" + drawAmount);

            // 修改余额
            balance -= drawAmount;
            System.out.println("此时余额：" + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " 取钱失败，余额不足！");
        }
    }
}
