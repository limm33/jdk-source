package other;
import org.junit.Test;

/*
 * 文件名：Test.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Test.java
 * 修改人：yunhai
 * 修改时间：2015年11月11日
 * 修改内容：新增
 */
/**
 * @author yunhai
 */
public class iPlusOrPlusi {
    final int time = 1000000000;

    @Test
    public void testAddCommon() {
        long timeS = System.currentTimeMillis();
        int i = 3;
        for (long j = 0; j < time; j++) {
            i = i + 1;
        }
        long timeE = System.currentTimeMillis();
        System.out.println("i=i+1结果：" + i + ",耗时：" + (timeE - timeS) + " ms");
    }

    @Test
    public void testPlusEquals() {
        long timeS = System.currentTimeMillis();
        int i = 3;
        for (long j = 0; j < time; j++) {
            i += 1;
        }
        long timeE = System.currentTimeMillis();
        System.out.println("i+=1结果：" + i + ",耗时：" + (timeE - timeS) + " ms");
    }

    @Test
    public void testFront() {
        long timeS = System.currentTimeMillis();
        int i = 3;
        for (long j = 0; j < time; j++) {
            i = ++i;
        }
        long timeE = System.currentTimeMillis();
        System.out.println("++i结果：" + i + ",耗时：" + (timeE - timeS) + " ms");
    }

    @Test
    public void testBack() {
        long timeS = System.currentTimeMillis();
        int i = 3;
        for (long j = 0; j < time; j++) {
            i++;
        }
        long timeE = System.currentTimeMillis();
        System.out.println("i++结果：" + i + ",耗时：" + (timeE - timeS) + " ms");
    }
}
