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
 * 将run方法内加入了synchronized同步代码块，使用account对象作为同步资源监视器。
 * 
 * 任何线程在修改account对象之前，首先要对account进行加锁，在account对象锁定期间，其他线程是无法修改该资源的。
 * 
 * 修改完成后，该线程释放对account资源的锁定。
 * 
 * @author yunhai
 */
public class DrawThread extends Thread {
    // 模拟用户账户
    private Account account;

    // 当前取钱线程所希望取的钱数
    private double drawAmount;

    public DrawThread(String name, Account account, double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    /**
     * 模拟多个线程同时取钱操作
     */
    @Override
    public void run() {
        // 使用account作为同步监视器，任何线程进入下面同步代码块之前
        // 先获得对account对象的锁定，其他线程无法获得锁，也就无法修改它
        // 符合：加锁--修改--释放锁逻辑
        synchronized (account) {
            // 判断账户余额大于取款金额
            if (account.getBalance() >= drawAmount) {
                System.out.println(getName() + "》》取钱成功，取款金额：" + drawAmount);

                // try { // 强制线程切换
                // sleep(1);
                // } catch (Exception e) {
                // e.printStackTrace();
                // }

                // 修改余额
                account.setBalance(account.getBalance() - drawAmount);
                System.out.println("此时余额：" + account.getBalance());
            } else {
                System.out.println(getName() + "》》取钱失败，余额不足！");
            }
        }
    }
}
