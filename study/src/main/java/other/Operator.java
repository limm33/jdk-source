/*
 * 文件名：Operator.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Operator.java
 * 修改人：yunhai
 * 修改时间：2015年11月12日
 * 修改内容：新增
 */
package other;

/**
 * 按位与&= 同真为真；
 * 
 * 按位或 |= 有真必真；
 * 
 * 按位异或^= 不同为真；
 * 
 * 按位取反~ （二进制非符号位1、0互变）
 * 
 * @author yunhai
 */
public class Operator {
    public static void main(String[] args) {
        boolean flag = true;
        flag &= true;
        System.out.println("true\t&=\ttrue\t==>\t" + flag);
        flag = true;
        flag &= false;
        System.out.println("true\t&=\tfalse\t==>\t" + flag);
        flag = false;
        flag &= true;
        System.out.println("false\t&=\ttrue\t==>\t" + flag);
        flag = false;
        flag &= false;
        System.out.println("false\t&=\tfalse\t==>\t" + flag + "\n");
        flag = true;
        flag |= true;
        System.out.println("true\t|=\ttrue\t==>\t" + flag);
        flag = true;
        flag |= false;
        System.out.println("true\t|=\tfalse\t==>\t" + flag);
        flag = false;
        flag |= true;
        System.out.println("false\t|=\ttrue\t==>\t" + flag);
        flag = false;
        flag |= false;
        System.out.println("false\t|=\tfalse\t==>\t" + flag + "\n");
        System.out.println("^=  相同为假，不同为真");
        flag = true;
        flag ^= true;
        System.out.println("true\t^=\ttrue\t==>\t" + flag);
        flag = true;
        flag ^= false;
        System.out.println("true\t^=\tfalse\t==>\t" + flag);
        flag = false;
        flag ^= true;
        System.out.println("false\t^=\ttrue\t==>\t" + flag);
        flag = false;
        flag ^= false;
        System.out.println("false\t^=\tfalse\t==>\t" + flag);
        System.out.println("101\t^=\t100==>\t1^1=0  0^0=0  1^0=1==>(101)^(100)=001");
        int a = 2, b = 5;
        a = a ^ b;
        b = a ^ b;// b = b ^ a;亦可，顺序无关
        a = a ^ b;
        System.out.println("两数交换：a=" + a + ",b=" + b);
    }
}