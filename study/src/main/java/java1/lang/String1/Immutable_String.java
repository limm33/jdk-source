/*
 * 文件名：quote.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： quote.java
 * 修改人：xiaofan
 * 修改时间：2016年3月20日
 * 修改内容：新增
 */
package java1.lang.String1;

import org.junit.Test;

/**
 * 方法传递时，基本数据类型传递副本，String不可变，修改相当于再次new，其他类型按引用传递。
 * 
 * @author xiaofan
 */
public class Immutable_String {
    StringBuffer str = new StringBuffer("good");

    String str1 = new String("good");

    char[] ch = {'a', 'b', 'c'};

    /**
     * String是 immutable的，一旦初始化，其引用指向的内容是不可变的.
     * 
     */
    @Test
    public void testString() {
        System.out.println("String：" + str1);
        Immutable_String ex = new Immutable_String();
        ex.changeStrBuffer(ex.str1, ex.ch); // 传递副本,另外开辟了一个空间用来存储
        System.out.print(ex.str1 + " and ");
        System.out.println(ex.ch);
    }

    @Test
    public void testStringBuffer() {
        System.out.println("StringBuffer：" + str);
        Immutable_String ex = new Immutable_String();
        ex.change(ex.str, ex.ch);
        System.out.print(ex.str + " and ");
        System.out.println(ex.ch);
    }

    private void changeStrBuffer(String str, char ch[]) {
        str = "test ok"; // String不可变，再次赋值相当于new
        ch[0] = 'g';
    }

    private void change(StringBuffer str, char ch[]) {
        str.setLength(0); // 清空StringBuffer
        str.append("test ok");
        ch[0] = 'g';
    }
}
