/*
 * 文件名：Integer_int.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Integer_int.java
 * 修改人：yunhai
 * 修改时间：2015年12月2日
 * 修改内容：新增
 */
package java1.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Integer and int.Integer初始值我null，int为0.
 * 
 * @author yunhai
 */
public class Integer_int {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        int i = 128;
        Integer i2 = 2;
        list.add(i2);
        list.add(i);
        for (Integer integer : list) {
            System.out.println(integer);
        }
        Integer i3 = 128;
        Integer i33 = new Integer(128);
        // Integer会自动拆箱为int，所以为true
        // int和integer(无论new否)比，都为true，因为会把Integer自动拆箱为int再去比
        System.out.println("int==Integer:" + (i == i3)); // true
        System.out.println("int==Integer:" + (i == i33)); // true
        Integer i4 = 127;
        Integer i5 = 127;
        // java在编译的时候,被翻译成-> Integer i5 = Integer.valueOf(127);
        // 对于-128=<Integer<=127之间的数，会进行缓存，Integer i4 = 127时，会将127进行缓存，
        // 下次再写Integer i5 = 127时，就会直接从缓存中取，就不会new了。所以i4 == i5的结果为true,而i6 == i7为false。
        System.out.println("Integer==Integer:" + (i4 == i5)); // true
        Integer i6 = 128;
        Integer i7 = 128;
        System.out.println("Integer==Integer:" + (i6 == i7)); // false
        // 无论如何，Integer与new Integer不会相等,内存地址不一样;同样，两个都是new出来的,都为false
        System.out.println("Integer==Integer:" + (i33 == i7)); // false
    }
    // public static Integer valueOf(int i) {
    // if (i >= IntegerCache.low && i <= IntegerCache.high)
    // return IntegerCache.cache[i + (-IntegerCache.low)];
    // return new Integer(i);
    // }
}
