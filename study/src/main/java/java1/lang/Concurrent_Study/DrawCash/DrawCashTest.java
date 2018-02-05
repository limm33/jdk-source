/*
 * 文件名：_2_DrawCash.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： _2_DrawCash.java
 * 修改人：yunhai
 * 修改时间：2015年11月17日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study.DrawCash;

import org.junit.Test;

/**
 * 银行取钱测试类.
 * 
 * 参考：http://blog.sina.com.cn/s/blog_4c925dca0100fetv.html
 * 
 * @author yunhai
 */
public class DrawCashTest {
    /**
     * 同步代码块.
     */
    @Test
    public void synCodeBlock() {
        // 新建我的账户，余额1000元
        Account account = new Account("我的账户", 1000);
        // 张三来取款800元
        new DrawThread("张三", account, 800).start();
        // 李四来取款800元
        new DrawThread("李四", account, 800).start();
    }

    /**
     * 同步方法.
     */
    @Test
    public void synMethod() {
        // 新建我的账户，余额1000元
        Account_Method account = new Account_Method("我的账户", 1000);
        // 张三来取款800元
        new DrawThread_Method("王五", account, 800).start();
        // 李四来取款800元
        new DrawThread_Method("赵六", account, 800).start();
    }
}
