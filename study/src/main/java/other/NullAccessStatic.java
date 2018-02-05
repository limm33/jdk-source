/*
 * 文件名：NullAccessStatic.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： NullAccessStatic.java
 * 修改人：yunhai
 * 修改时间：2015年12月8日
 * 修改内容：新增
 */
package other;

/**
 * Java类只能包含：成员变量、方法、构造器、初始化块、内部类（包括接口、枚举）；
 * 
 * 以static修饰的成员就是【类成员】，static除构造器外均可修饰；类成员属于整个类，而不属于单个对象；
 * 
 * 为null的实例，可以访问其所属类的类成员，而不能访问非类成员（NullPointerException）。
 * 
 * @author yunhai
 */
public class NullAccessStatic {
    private String name;

    private static int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        NullAccessStatic.age = age;
    }

    private static void test() {
        System.out.println("static 修饰的类方法");
    }

    public static void main(String[] args) {
        // 定义NullAcessStatic变量，值为null
        NullAccessStatic nas = null;
        // null调用所属类的静态方法
        nas.test(); // Output:static 修饰的类方法
        nas.setAge(2);
        System.err.println(nas.getAge()); // Output:2【 err 立即输出结果】
        System.err.println(nas.age); // Output:2【通过类对象访问】
        nas.setName("hi"); // Exception: java.lang.NullPointerException
        System.err.println(nas.getName());
    }
}
