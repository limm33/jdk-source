package other;

import java.util.ArrayList;
import java.util.List;

/*
 * 文件名：SizeOrLenrth.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： SizeOrLenrth.java
 * 修改人：yunhai
 * 修改时间：2015年10月9日
 * 修改内容：新增
 */

/**
 * 何时用size，何时用length
 * 
 * 1 java中的length属性是针对数组说的,比如说你声明了一个数组,想知道这个数组的长度则用到了length这个属性.
 * 
 * 2 java中的length()方法是针对字符串String说的,如果想看这个字符串的长度则用到length()这个方法.
 * 
 * 3.java中的size()方法是针对泛型集合说的,如果想看这个泛型有多少个元素,就调用此方法来查看!
 * 
 * @author yunhai
 */
public class SizeOrLenrth {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        int[][] b = {{1, 2}, {3, 4, 5}, {6}};
        String str = "love";
        List list = new ArrayList();
        list.add("ll");
        System.out.println(a.length);
        System.out.println(b.length);
        System.out.println(b[0].length);
        System.out.println(str.length());
        System.out.println(list.size());
    }
}
