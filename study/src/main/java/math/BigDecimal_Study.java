/*
 * 文件名：BigDecimal_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： BigDecimal_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月21日
 * 修改内容：新增
 */
package math;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * BigDecimal定义了一下舍入模式，只有在作除法运算或四舍五入时才用到舍入模式.
 * 
 * 舍入模式，网上很多资料都不够清楚（尤其是ROUND_HALF_DOWN、ROUND_HALF_UP），这里自己看了源码，参考前人的基础，整理了一下。
 * 
 * 【ROUND_CEILING】: Rounding mode to round towards positive infinity. 向正无穷方向舍入;
 * 
 * 【ROUND_DOWN】:Rounding mode to round towards zero. 向零方向舍入;
 * 
 * 【ROUND_UP】:Rounding mode to round away from zero
 * 
 * 【ROUND_FLOOR】: Rounding mode to round towards negative infinity.向负无穷方向舍入;
 * 
 * 【ROUND_HALF_DOWN】 :Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round down. Behaves as for ROUND_UP if the discarded fraction is > 0.5;
 * otherwise, behaves as for ROUND_DOWN.
 * 
 * 四舍五入；舍弃部分>0.5,则ROUND_UP (3.125-->3.12)(3.1251-->3.13)
 * 
 * 【ROUND_HALF_EVEN】:Rounding mode to round towards the "nearest neighbor" unless both neighbors are equidistant, in which case, round towards the even neighbor.
 * 
 * 四舍五入；如果保留位数是奇数，使用ROUND_HALF_UP ，如果是偶数，使用ROUND_HALF_DOWN;
 * 
 * 【ROUND_HALF_UP】: Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
 * 
 * 四舍五入；舍弃部分>=0.5,则ROUND_UP（3.125-->3.13）
 * 
 * 【ROUND_UNNECESSARY】:Rounding mode to assert that the requested operation has an exact result, hence no rounding is necessary.
 * 
 * 计算结果是精确的，不需要舍入模式; 如果对获得精确结果的操作指定[非精确]舍入模式，则抛出ArithmeticException。
 * 
 * @author yunhai
 */
public class BigDecimal_Study {
    final int DEF_DIV_SCALE = 10;// 除法默认运算精度

    BigDecimal o1 = new BigDecimal("5.60");

    BigDecimal o2 = new BigDecimal("2.6");

    BigDecimal o3 = new BigDecimal("6.25"); // 3.125

    BigDecimal o4 = new BigDecimal("2");

    BigDecimal o5 = new BigDecimal("2.3"); // 1.15

    /**
     * 四则运算及舍入模式细节.
     */
    @Test
    public void fourOperations() {
        System.out.println(o1.add(o2));
        System.out.println(o1.subtract(o2));
        System.out.println(o1.multiply(o2));
        System.out.println(o1.divide(o2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP));// 2.153846153846154
        System.out.println(o1.divide(o2, 5, BigDecimal.ROUND_UP));// 2.15385
        System.out.println(o3.divide(o4, 2, BigDecimal.ROUND_HALF_DOWN));// 3.125-->3.12
        System.out.println(new BigDecimal("6.2502").divide(o4, 2, BigDecimal.ROUND_HALF_DOWN));// 3.1251-->3.13
        System.out.println(o3.divide(o4, 2, BigDecimal.ROUND_HALF_UP));// 3.125-->3.13
        System.out.println(o3.divide(o4, 2, BigDecimal.ROUND_HALF_EVEN));// 3.12
        System.out.println(o5.divide(o4, 1, BigDecimal.ROUND_HALF_EVEN));// 1.2
        System.out.println(o5.divide(o4, 2, BigDecimal.ROUND_UNNECESSARY));// 1.15
        // System.out.println(o5.divide(o4, 1, BigDecimal.ROUND_UNNECESSARY));//
        System.out.println(new BigDecimal("1.12").setScale(5, BigDecimal.ROUND_UNNECESSARY)); // 1.12000
        // o3.setScale(1, RoundingMode.UNNECESSARY); // java.lang.ArithmeticException: Rounding necessary
        o5.divide(o4, BigDecimal.ROUND_UNNECESSARY); // java.lang.ArithmeticException: Rounding necessary
    }

    @Test
    /**
     * BigDecimal 对象的值是不可变的.
     */
    public void immutableTest() {
        BigDecimal a1 = new BigDecimal("1.11");
        BigDecimal b1 = new BigDecimal("2.22");
        a1.add(b1);
        System.out.println("a plus b is : " + a1); // a plus b is : 1.11
        BigDecimal res = a1.add(b1);
        System.out.println("a plus b is : " + res); // a plus b is : 3.33
        System.out.println("a plus b is : " + a1.add(b1));// a plus b is : 3.33
        System.out.println(b1.plus()); // 参数为空，返回本身this (2.22)
    }
}
