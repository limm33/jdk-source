/*
 * 文件名：Implements_Java8.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Implements_Java8.java
 * 修改人：yunhai
 * 修改时间：2016年3月21日
 * 修改内容：新增
 */
package other;

import org.junit.Test;

/**
 * Java8中implements新特性default
 * 
 * Java8的Default方法：接口新增default方法，而不破坏现有实现架构。【无缝结合Lambda表达式】
 * 
 * @author yunhai
 */
interface A {
    String sys(); // 传统接口

    default void sysNew() { // default关键字,所有实现此接口的类都会默认继承这个方法
        System.out.println("new sysout");
    }
}

public class Implements_Java8 implements A {

    /**
     * {@inheritDoc}.
     */
    @Override
    public String sys() {
        System.out.println("sysout");
        return null;
    }
    //
    // @Override
    // public void sysNew() {
    // System.out.println("sysout-Override");
    // }

    @Test
    public void test() {
        sys();
        sysNew(); // 直接使用，不必重写；重写亦可
    }
}
