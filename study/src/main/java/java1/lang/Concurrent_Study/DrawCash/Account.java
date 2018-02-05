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
public class Account {
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
    public Account(String accountNo, int balance) {
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

    public void setBalance(double d) {
        this.balance = d;
    }
}
