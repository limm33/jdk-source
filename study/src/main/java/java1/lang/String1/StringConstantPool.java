/*
 * 文件名：StringConstantPool.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： StringConstantPool.java
 * 修改人：yunhai
 * 修改时间：2016年3月10日
 * 修改内容：新增
 */
package java1.lang.String1;

import org.junit.Test;

/**
 * String 常量池
 * 
 * 字符串常量池(String pool, String intern pool, String保留池) 是Java堆内存中一个特殊的存储区域,
 * 
 * 当创建一个String对象时,假如此字符串值已经存在于常量池中,则不会创建一个新的对象,而是引用已经存在的对象。
 * 
 * d=b+c:先执行StringBuilder的拼接，相当于new了一下，虽然值相等，但内存地址已变.
 * 
 * 当Java能直接使用字符串直接量（包括在编译时就计算出来的字符串值时，如String e = "张" + "三";），JVM就会使用常量池来管理这些字符串。
 * 
 * @author yunhai
 */
public class StringConstantPool {
    @Test
    public void test() {
        String a = "张三";
        String b = "张";
        String c = "三";
        String d = b + c;
        System.out.println(a == d); // false
        String e = "张" + "三";
        System.out.println(a == e); // true
        // 强制使用字符串常量池
        // java.lang.String的intern()方法，"abc".intern()方法的返回值还是字符串"abc".作用：检查字符串池里是否存在"abc"这么一个字符串，如果存在，就返回池里的字符串；如果不存在，该方法会 把"abc"添加到字符串池中，然后再返回它的引用。
        d = d.intern(); // 需要再次赋值给d，因为intern返回的是常量池的值或者一个引用
        System.out.println(a == d); // true
    }

    @Test
    public void final_String() {
        String a = "张三";
        final String b = "张"; // 只有在编译期间能确切知道final变量值的情况下,才会将b当成编译器常量
        String d = b + "三";
        System.out.println(a == d); // true
        String c = getC();
        String e = c + "三";
        System.out.println(a == e); // false，编译时不确定变量的值
    }

    private String getC() {
        return "张";
    }
}
