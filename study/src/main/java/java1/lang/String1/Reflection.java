/*
 * 文件名：Reflection.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Reflection.java
 * 修改人：yunhai
 * 修改时间：2015年11月12日
 * 修改内容：新增
 */
package java1.lang.String1;

import java.lang.reflect.Field;

/**
 * 反射修改String。
 * 
 * 用反射访问私有成员， 可以反射出String对象中的value属性， 进而通过改变获得的value引用改变数组的结构。
 * 
 * @author yunhai
 */
public class Reflection {
    public static void main(String[] args) throws Exception {
        // 创建字符串"Hello World"，并赋给引用s
        String s = "Hello World";
        System.out.println("原  s= " + s + " ,hash：" + s.hashCode()); // 原s= Hello World 地址：-862545276
        commonChange(s);
        System.out.println("======反射修改======");
        // 获取String类中的value字段
        Field valueField = String.class.getDeclaredField("value");
        // 改变value属性的访问权限
        valueField.setAccessible(true);
        // 获取s对象上的value属性的值
        char[] value = (char[]) valueField.get(s);
        // 改变value所引用的数组中的第5个字符
        // value[5] = '_';
        valueField.set(s, new char[]{'h', 'a', 'p', 'p', 'y'});
        System.out.println("反射s= " + s + " ,hash：" + s.hashCode()); // 新s= happy 地址：-862545276

    }

    private static void commonChange(String s) {
        System.out.println("======普通修改======");
        s = "Hello--World";
        System.out.println("改变s= " + s + " ,hash：" + s.hashCode()); // 改变s= Hello--World ,hash：753841376
        String newStr = new String("Hello--World");
        System.out.println("新  s= " + newStr + " ,hash：" + newStr.hashCode()); // 新 s= Hello--World ,hash：753841376【内存中已存在】
        System.out.println(s == newStr); // false 【虽然hash相同】
    }
}
