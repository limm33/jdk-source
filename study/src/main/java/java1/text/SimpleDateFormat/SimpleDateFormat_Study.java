/*
 * 文件名：SimpleDateFormat_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： SimpleDateFormat_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月23日
 * 修改内容：新增
 */
package java1.text.SimpleDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.junit.Test;

/**
 * SimpleDateFormat:是DateFormat的子类，更简单灵活功能更强大。【text包下】
 * 
 * SimpleDateFormat extends DateFormat均【非线程安全】。
 * 
 * G 年代标志符；y 年；M 月；d 日；h 时 12小时制；H 时 24小时制；m 分；s 秒；S 毫秒；E 星期；
 * 
 * D 一年中的第几天；F 一月中第几个星期几；w 一年中第几个星期；W 一月中第几个星期；
 * 
 * a 上午 / 下午 标记符；k 时 在一天中 (1~24)；K 时 在上午或下午 (0~11)；z 时区 *
 * 
 * ----多线程直接使用SimpleDateFormat会报异常，非线程安全-----
 * 
 * 解决方案：1、每次调用都new SimpleDateFormat；【最慢】
 * 
 * 2、同步SimpleDateFormat对象；
 * 
 * 3、使用ThreadLocal；【最快】
 * 
 * 4、抛弃JDK，使用其他类库中的时间格式化类：
 * 
 * 　　1.使用Apache commons 里的FastDateFormat，宣称是既快又线程安全的SimpleDateFormat, 但只能对日期进行format, 不能对日期串进行解析。
 * 
 * 　　2.使用Joda-Time类库来处理时间相关问题
 * 
 * @author yunhai
 */
public class SimpleDateFormat_Study {
    final int NUM_THREAD = 3;

    static String str = "";

    @Test
    public void formatTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年的第D天");
        Date date = new Date();
        String str = sdf.format(date);
        System.out.println(str);
    }

    @Test
    public void parseTest() throws ParseException {
        String dateStr = "#15@@三月##12%23**61";
        SimpleDateFormat sdf = new SimpleDateFormat("#yyyy@@MMM##d%m"); // Tue Mar 12 00:23:00 CST 15
        System.out.println(sdf.parse(dateStr));
        // 默认：字段超出范围，自动进位
        SimpleDateFormat sdf1 = new SimpleDateFormat("#yyyy@@MMM##d%m**s"); // Tue Mar 12 00:24:01 CST 15
        System.out.println(sdf1.parse(dateStr));
        sdf1.setLenient(false); // 容错性
        System.out.println(sdf1.parse(dateStr)); // ParseException
    }

    /**
     * Junit下多线程Test.
     * 
     * @throws Throwable
     */
    @Test
    public void threadTest() throws Throwable {
        TestRunnable[] testRunnables = new TestRunnable[NUM_THREAD];
        for (int i = 0; i < testRunnables.length; i++) {
            // testRunnables[i] = new ThreadSimple(); // 报异常java.lang.NumberFormatException
            testRunnables[i] = new ThreadSync();
            // testRunnables[i] = new ThreadLocal();
        }
        final MultiThreadedTestRunner multiThreadedTestRunner = new MultiThreadedTestRunner(testRunnables);
        multiThreadedTestRunner.runTestRunnables();
    }

    /**
     * 直接多线程。
     */
    private static class ThreadSimple extends TestRunnable {
        @Override
        public void runTest() {
            try {
                System.out.println((str = this.toString()).substring(str.indexOf("$")) + ":" + SimpleDateFormatUtil.parse("2015-12-23 15:00:00"));
                // System.out.println(this.toString()); // 正常输出所有线程名
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步SimpleDateFormat对象。
     */
    private static class ThreadSync extends TestRunnable {
        @Override
        public void runTest() {
            try {
                System.out.println((str = this.toString()).substring(str.indexOf("$")) + ":" + SimpleDateFormatUtil.parseSync("2015-12-23 15:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步SimpleDateFormat对象。
     */
    private static class ThreadLocal extends TestRunnable {
        @Override
        public void runTest() {
            try {
                System.out.println((str = this.toString()).substring(str.indexOf("$")) + ":" + SimpleDateFormatUtil.parseLocal("2015-12-23 15:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
