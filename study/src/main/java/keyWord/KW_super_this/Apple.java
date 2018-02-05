/*
 * 文件名：ParentTest.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ParentTest.java
 * 修改人：yunhai
 * 修改时间：2015年12月18日
 * 修改内容：新增
 */
package keyWord.KW_super_this;

import org.junit.Test;

/**
 * 构造函数，super、this的用法
 * 
 * @author yunhai
 */
class Plant {
    protected Plant() { // 不能为private
        System.out.println("plant父类无参构造器");
    }
}

class Fruit extends Plant {
    public Fruit(String name) {
        System.out.println("fruit--1-个参数构造器" + name);
    }

    public Fruit(String name, double weight) {
        this(name);
        System.out.println("fruit--2-个参数构造器" + weight);
    }
}

public class Apple extends Fruit {

    public Apple() {
        super("apple", 1); // 显式调用父类构造器
        System.out.println("Apple无参构造器");
    }

    @Test
    public void test() {
        new Apple(); // 还没进入Apple构造器就已经加载了相关构造器，会输出两遍。
    }

    public static void main(String[] args) { // 构造器测试用main方法
        new Apple();
    }
    // plant父类无参构造器
    // fruit--1-个参数构造器apple
    // fruit--2-个参数构造器1.0
    // Apple无参构造器
}
