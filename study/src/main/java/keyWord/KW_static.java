/*
 * 文件名：Static_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Static_Study.java
 * 修改人：yunhai
 * 修改时间：2015年11月10日
 * 修改内容：新增
 */
package keyWord;

/**
 * @author yunhai
 */
// 两个类
public class KW_static {// 类名与文件名StaticTest.java一致
    public static void main(String args[]) {
        // System.out.println(Test.X);
        // Test.display();
        // Test t = new Test();
        try {
            Class.forName("Test");
            Class.forName("Test");
            // Class.forName("Test", false, StaticTest.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Test {// 一个Java文件中只能有一个public类
    public static int X = 100;

    public final static int Y = 200;

    public Test() {
        System.out.println("Test构造函数执行");
    }

    static {
        System.out.println("static语句块执行");
    }

    public static void display() {
        System.out.println("静态方法被执行");
    }

    public void display_1() {
        System.out.println("实例方法被执行");
    }
}