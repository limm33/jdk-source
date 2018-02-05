/*
 * 文件名：Objects_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Objects_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月21日
 * 修改内容：新增
 */
package java1.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * equals：null,null返回true。
 * 
 * @author yunhai
 */
class User {
    String name;

    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

@SuppressWarnings("unchecked")
public class Objects_Study {
    /**
     * null调用toString方法返回null.
     * 
     * compare:a==b（包括a、b均为null），皆返回0；仅一个参数为null，may or may not抛出空指针异常。
     */

    @Test
    public void test() {
        System.out.println(Objects.toString(null)); // null
        System.out.println(Objects.compare(null, null, null)); // 0
        // System.out.println(Objects.compare(1, null, null)); // NullPointerException
        // System.out.println(Objects.requireNonNull(null)); // null则NullPointerException，否则返回原obj
        System.out.println(Objects.isNull(null)); // true
        System.out.println(Objects.nonNull(null)); // false
        List list = null;
        // Objects.requireNonNull(list, "list is null"); // null则报null异常，并在NullPointerException后显示message，否则返回原obj
        Objects.requireNonNull(null, new Supplier() { // null则报null异常，并在NullPointerException后显示Supplier.get（译：供应商）的信息，否则返回原obj
                    @Override
                    public String get() {
                        return "this is null【Supplier】";
                    }
                });
    }

    /**
     * 单层数据，equals和deepEquals无区别.
     * 
     */
    @Test
    public void singlelayer() {
        List list1 = new ArrayList();
        list1.add(1);
        list1.add("zs");

        List list2 = new ArrayList();
        list2.add(1);
        list2.add("zs");
        assertEquals(Objects.equals(list1, list2), true); // OK
        assertEquals(Objects.deepEquals(list1, list2), true);
    }

    /**
     * 【8种基本数据类型 或 地址能==】嵌套，用deepEquals才可能返回true.
     * 
     */
    @Test
    public void deepArray() {
        int[][] a = {{1, 2, 3}, {4, 5, 6}};
        int[][] b = {{1, 2, 3}, {4, 5, 6}};
        List list1 = new ArrayList(Arrays.asList(1, "s")); // 非嵌套
        List list2 = new ArrayList(Arrays.asList(1, "s"));
        // assertEquals(Objects.equals(a, b), true); // fail
        assertEquals(Objects.deepEquals(a, b), true); // sussess
        assertEquals(Objects.equals(list1, list2), true); // sussess
        assertEquals(Objects.deepEquals(list1, list2), true); // sussess
    }

    /**
     * 嵌套，包含非基本数据类型且地址不能==，均返回false. Arrays.deepEquals0
     * 
     */
    @Test
    public void deepNonArray() {
        List list1 = new ArrayList();
        list1.add(1);
        list1.add(new User("zs", 10));
        List list2 = new ArrayList();
        list2.add(1);
        list2.add(new User("zs", 10));
        System.out.println(Objects.equals(list1, list2)); // false
        System.out.println(Objects.deepEquals(list1, list2)); // false
    }
}