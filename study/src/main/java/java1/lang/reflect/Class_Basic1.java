/*
 * 文件名：Class_Basic1.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Class_Basic1.java
 * 修改人：xiaofan
 * 修改时间：2017年2月19日
 * 修改内容：新增
 */
package java1.lang.reflect;

/**
 * @author xiaofan
 */
public class Class_Basic1 {

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        // Fruit实例对象表示方式
        Fruit fruit = new Fruit();
        // 任何一个类都是Class的实例对象（成为该类的类类型），这个实例对象有3种表示方式：
        // ① 任何一个类都有一个隐含的静态成员变量
        Class c1 = Fruit.class;
        // ② 类对象的getClass方法
        Class c2 = fruit.getClass();
        // c1/c2：Fruit类的类类型（class type）
        System.out.println(c1 == c2); // true
        // ③ forName
        Class c3 = null;
        try {
            c3 = Class.forName("java1.lang.reflect.Fruit");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(c2 == c3); // true
        // 通过类的类类型（c1、c2、c3）创建类的创建类的对象实例。
        try {
            Fruit fruit2 = (Fruit) c1.newInstance(); // 需要有无参的构造方法
            fruit2.getColour();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Fruit {
    void getColour() {
        System.out.println("Hi colour");
    }
}