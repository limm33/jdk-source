/*
 * 文件名：ClassMethod.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ClassMethod.java
 * 修改人：xiaofan
 * 修改时间：2017年2月26日
 * 修改内容：新增
 */
package java1.lang.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Class方法的反射。
 * 
 * 方法名和参数列表唯一决定某个方法（method.invoke(对象，参数列表（可能无）)）
 * 
 * @author xiaofan
 */
public class ClassMethod extends TestCase {
    /**
     * 使用Method.invoke调用方法.
     * 
     */
    public void testInvokemethod() {
        A a = new A();
        Class c = a.getClass(); // 获取类的方法《--类信息《--类类型
        // 方法：由名称和参数列表决定
        // getMethod获取public方法
        //
        try {
            // Method method = c.getDeclaredMethod("print", new Class[]{int.class, int.class});
            Method method_int = c.getDeclaredMethod("print", int.class, int.class); // 等价于new Class[]{int.class, int.class}
            // 方法method没有返回值则返回null，否则需要强转
            Object invokeReturn = method_int.invoke(a, new Object[]{10, 20}); // 等价于 a.print(10,20);
            System.out.println("强转:" + (int) invokeReturn);
            System.out.println("==========");
            Method method_String = c.getDeclaredMethod("print", String.class, String.class);
            method_String.invoke(a, "hi", "zxiaofan"); // 即a.print("hi","zxiaofan")
            System.out.println("==========");
            // Method method_NoParam = c.getDeclaredMethod("print", new Class[]{});
            Method method_NoParam = c.getDeclaredMethod("print"); // 等价于("print", new Class[]{})
            method_NoParam.invoke(a, new Object[]{});
            method_NoParam.invoke(a); // 等价于invoke(a, new Object[]{})
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射与泛型.
     * 
     * 通过反射插入集合的非相同类型的数据，只能直接处理Object或对Object强转到实际类型。
     */
    @SuppressWarnings("rawtypes")
    public void testGeneric() {
        List list0 = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("hi");
        // list.add(123); // 编译报错
        Class c0 = list0.getClass();
        Class c = list.getClass();
        assertEquals(c0, c); // true
        // c0==c1说明：编译之后集合的泛型是去泛型化的；java中的泛型是防止错误输入的，只在编译阶段有效，编译后无效
        // 通过方法的反射绕过编译
        Method method;
        try {
            method = c.getMethod("add", Object.class);
            method.invoke(list, 456); // 绕过编译操作就绕过了泛型
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Object obj : list) { // 这样遍历是可以的
            System.out.println(obj);
        }
        System.out.println("======");
        for (String str : list) {
            System.out.println(str); // 不能这样遍历,Iterator迭代也不行
            // str为int时抛异常：java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
        }

    }
}

class A {
    public void print() { // 无参无返回值
        System.out.println("hi zxiaofan.com");
    }

    public String print(String a, String b) { // 参数String返回String
        String c = a + b;
        System.out.println(c);
        return c;
    }

    public int print(int a, int b) { // 参数int返回int
        int c = a + b;
        System.out.println(c);
        return c;

    }
}