/*
 * 文件名：TimeUnit_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TimeUnit_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月18日
 * 修改内容：新增
 */
package java1.util.concurrent;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * TimeUnit是并发包内置的枚举类，sleep或是时间转换都相当方便。
 * 
 * 那么TimeUnit的sleep效率上和Thread.sleep有区别吗？几乎没区别，甚至更快。
 * 
 * http://note.youdao.com/share/?id=d8d6d69328bc1f2edbfee47d9aa73a98&type=note
 * 
 * @author yunhai
 */
public class TimeUnit_Study {
    @Test
    public void testSleep() {
        System.err.println("准备调用TimeUnit休眠...");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("睡眠结束");
    }

    @Test
    public void testConvert() {
        /*
         * TimeUnit是一个枚举类型,可以将时间方便快捷的转换为(天、时、分、秒、纳秒)day,hour,minute,second,millli... 有了这个类我们可以方便将时间进行转换
         */
        // 1、我们将1个小时转换为多少分钟、多少秒
        System.out.println(TimeUnit.HOURS.toMinutes(1)); // =>60
        System.out.println(TimeUnit.HOURS.toSeconds(1)); // =>3600
        // 2、如果将秒转换为小时、分钟呢
        System.out.println(TimeUnit.SECONDS.toMinutes(3600));// =>60
        System.out.println(TimeUnit.SECONDS.toHours(3600)); // =>1
    }
}
