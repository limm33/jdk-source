/*
 * 文件名：EnumSet_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： EnumSet_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月29日
 * 修改内容：新增
 */
package java1.util.set;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Test;

/**
 * EnumSet
 * 
 * ①add-null抛空指针异常，判断是否包含null和removenull无异常；remove-null时返回false。
 * 
 * ②内部以位向量形式存储，紧凑高效，so占用内存小且运行效率高(尤其是批量操作，如containsAll、retainAll)
 * 
 * @author yunhai
 */
enum Season {
    Spring, summer, fail, winter
}

enum num {
    a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34, a35, a36, a37, a38, a39, a40, a41, a42, a43, a44, a45, a46, a47, a48, a49, a50, a51, a52, a53, a54, a55, a56, a57, a58, a59, a60, a61, a62, a63, a64, a65, a66, a67, a68, a69
}

public class EnumSet_Study {
    @Test
    public void init() {
        // 创建一个EnumSet集合，元素是Season枚举的所有枚举值
        EnumSet set = EnumSet.allOf(Season.class);
        System.out.println(set); // [Spring, summer, fail, winter]
        // 创建一个集合元素是Season枚举值的空EnumSet
        set = EnumSet.noneOf(Season.class);
        System.out.println(set); // []
        set.add(Season.summer);
        set.add(Season.summer); // 值不重复，但add相同不抛异常
        set.add(Season.Spring); // 输出顺序为枚举类Enum的顺序
        System.out.println(set); // [summer]
        // of 指定枚举值，初始化Set
        EnumSet set2 = EnumSet.of(Season.Spring, Season.summer);
        System.out.println("of-初始化Set: " + set2); // [Spring, summer]
        // 从from到to的所有枚举值组成的集合
        set2 = EnumSet.range(Season.summer, Season.winter);
        System.out.println(set2); // [summer, fail, winter]
        // set2+set3=Season全部枚举值
        EnumSet set3 = EnumSet.complementOf(set2);
        System.out.println(set3); // [Spring]
        set3.contains(Season.summer);
        System.out.println("size:" + set.size());
    }

    @Test
    public void copy() {
        EnumSet set = EnumSet.of(Season.Spring, Season.summer);
        EnumSet set2 = EnumSet.copyOf(set);
        System.out.println(set2); // [Spring, summer]
        Collection col = new HashSet();
        col.add(Season.Spring);
        col.add(Season.fail);
        set2 = EnumSet.copyOf(col);
        System.out.println(set2); // [Spring, fail]
        col.add("Smile");
        // copy collection的元素创建EnumSet集合时，必须保证collection的元素全为同一个枚举类的枚举值
        set2 = EnumSet.copyOf(col); // 异常ClassCastException
    }

    @Test
    public void testRegularEnumSet() {
        EnumSet set = EnumSet.noneOf(num.class); // 创建空集合
        // set = set.range(num.a0, num.a69); // elements只有0、1位有值。
        set.add(num.a0); // a0.ordinal=0,eWordNum=0;使ele[0]=1;
        set.add(num.a1); // a1.ordinal=1,eWordNum=0；1L << eOrdinal=2;elements[eWordNum] |= (1L << eOrdinal) 即第0个元素与2或等于，使elements[0]=11(B);
        set.add(num.a50);
        set.add(num.a63);
        set.add(num.a64);
        set.add(num.a65); // a65.ordinal=65，eWordNum=1；第1个元素与2或等于，使ele[1]=2；
        set.add(num.a69);
        // elements[0]的值的二进制 第i位为1，则表示Enum的第i个元素存在于该集合，
        // 引申[64*j +i]，elements[j]的第i个元素为1。
        System.out.println(set.contains(num.a65)); // 65>>>6=1; 1<<65=2; ele[1]&2=35&2=2
    }
    // 例--contains：
    // "eOrdinal" 65
    // "1L << eOrdinal" 2
    // "eOrdinal >>> 6" 1 // 待查元素的高位
    // "elements[eOrdinal >>> 6]" 35 // elements第j号位所有元素
    // "elements[eOrdinal >>> 6] & (1L << eOrdinal)" 2
}
