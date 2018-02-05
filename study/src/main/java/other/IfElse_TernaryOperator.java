/*
 * 文件名：IfElse_TernaryOperator.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： IfElse_TernaryOperator.java
 * 修改人：yunhai
 * 修改时间：2015年11月18日
 * 修改内容：新增
 */
package other;

/**
 * 比较if else和三目运算符的效率。
 * 
 * 本程序结果堪忧，参考http://bbs.csdn.net/topics/350229715
 * 
 * 应该是三目更快。
 * 
 * @author yunhai
 *
 */
public class IfElse_TernaryOperator {

    public static void main(String[] args) {
        double times = 20.0;
        double timeIf = 0;
        double timeTer = 0;
        for (int i = 0; i < times; i++) {
            timeIf += testIfElse();
        }
        for (int i = 0; i < times; i++) {
            timeTer += testTer();
        }
        System.out.println("IfElse:" + (timeIf / times));
        System.out.println("TernaryOperator:" + (timeTer / times));
    }

    private static long testIfElse() {
        int k = 100000000;
        int temp = 0;
        long timeS = System.currentTimeMillis();
        for (int i = 0; i < k; i++) {
            if (temp == 6666)
                temp = i + 1;
            else
                temp = i + 0;
        }
        long timeE = System.currentTimeMillis();
        return timeE - timeS;
    }

    private static long testTer() {
        int k = 100000000;
        int temp = 0;
        long timeS = System.currentTimeMillis();
        for (int i = 0; i < k; i++) {
            temp = temp == 6666 ? i + 1 : i + 0;
        }
        long timeE = System.currentTimeMillis();
        return timeE - timeS;
    }
}
