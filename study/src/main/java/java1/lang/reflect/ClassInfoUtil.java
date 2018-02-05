/*
 * 文件名：ClassInfoUtil.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ClassInfoUtil.java
 * 修改人：xiaofan
 * 修改时间：2017年2月21日
 * 修改内容：新增
 */
package java1.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * 通过反射获取Class相关信息.
 * 
 * 获取类信息，需先获取类的类类型 Class c = obj.getClass();
 * 
 * @author xiaofan
 */
public class ClassInfoUtil extends TestCase {
    /**
     * 测试打印方法信息.
     * 
     */
    public void testPrintClassMethodInfo() {
        Class c1 = int.class;
        Class c2 = String.class;
        Class c3 = void.class;
        System.out.println(c1.getName()); // 类的全称（int）
        System.out.println(c2.getName()); // java.lang.String
        System.out.println(c2.getSimpleName()); // 不包含包名的类名称（String）
        System.out.println(c3.getName()); // void
        // 基本数据类型、void关键字，都存在类类型
        String str = "zxiaofan.com";
        System.out.println("=====打印  " + str.getClass().getName() + "  的类信息=====");
        printClassMethodInfo(str);
    }

    /**
     * 测试打印成员变量信息.
     * 
     */
    public void testFieldInfo() {
        printFieldInfo(new Integer("1"));
    }

    /**
     * 测试打印构造函数信息.
     * 
     */
    public void testConstructorInfo() {
        printConstructorInfo(new Integer("1"));
    }

    /**
     * 打印任意类的信息（类的成员函数、成员变量）.
     * 
     */
    @SuppressWarnings("rawtypes")
    public static void printClassMethodInfo(Object obj) {
        // 获取类信息，需先获取类的类类型
        Class c = obj.getClass();
        // 获取类名称
        System.out.println("类全称是：" + c.getName());
        // Method类，方法对象，一个成员对象就是一个Method对象；
        // getMethods()获取所有public的函数，包括父类继承而来的
        // c.getDeclaredMethods()获取所有该类自己声明的方法（所有访问类型）
        Method[] ms = c.getMethods(); // c.getDeclaredMethods()
        System.out.println("类方法如下：");
        for (int i = 0; i < ms.length; i++) {
            Method method = ms[i];
            // 方法返回值类型的类类型
            Class returnType = method.getReturnType();
            System.out.print("__" + returnType.getName() + " ");
            // 方法名称
            System.out.print(method.getName() + "(");
            // 获取参数类型（参数列表的类型的类类型）
            Class[] paramTypes = method.getParameterTypes();
            for (Class class1 : paramTypes) {
                System.out.print(class1.getName() + ",");
            }
            System.out.println(")");
        }
    }

    /**
     * 打印任意类的成员变量信息.
     * 
     * 成员变量也是对象，java.lang.Field类封装了成员变量相关操作。
     * 
     * getFields()：所有public的成员变量；
     * 
     * getDeclaredFields():该类自己声明的（即所有）成员变量信息。
     * 
     */
    @SuppressWarnings("rawtypes")
    public static void printFieldInfo(Object obj) {
        // 获取类信息，需先获取类的类类型
        Class c = obj.getClass();
        // Field[] fields=c.getFields();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            // 成员变量的类型的类类型
            Class fieldType = field.getType();
            String typeName = fieldType.getName();
            // 成员变量的名称
            String fieldName = field.getName();
            System.out.println(typeName + " " + fieldName);
        }
    }

    /**
     * 打印任意类的构造函数信息.
     * 
     * 构造函数也是对象。java.lang.Constructor封装了构造函数信息
     * 
     * getConstructors()：所有public的构造函数
     * 
     * getDeclaredConstructors()：所有构造函数
     * 
     * getEnclosingConstructor()：类A构造函数定义了内部类InnerA，则通过InnerA的Class对象调用getEnclosingConstructor可获取类A的构造函数（不是构造函数列表）
     * 
     */
    @SuppressWarnings("rawtypes")
    public static void printConstructorInfo(Object obj) {
        // 获取类信息，需先获取类的类类型
        Class c = obj.getClass();
        // Constructor[] constructors = c.getConstructors();
        // Constructor enclosingConstructor = c.getEnclosingConstructor();
        Constructor[] constructors = c.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            System.out.print(constructor.getName() + " (");
            Class[] paramTypes = constructor.getParameterTypes();
            for (Class class1 : paramTypes) {
                System.out.print(class1.getName() + ",");
            }
            System.out.println(")");
        }
    }
}
