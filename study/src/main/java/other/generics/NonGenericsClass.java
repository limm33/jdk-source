/*
 * 文件名：NonGenericClass.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： NonGenericClass.java
 * 修改人：yunhai
 * 修改时间：2015年12月31日
 * 修改内容：新增
 */
package other.generics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * NonGenericsClass：并不存在泛型类。
 * 
 * 不管泛型的实际类型是什么，运行时总有相同的类（class），在内存中也只占用一块内存空间。
 * 
 * @author yunhai
 */
public class NonGenericsClass {
    @Test
    public void test() {
        List<String> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        // 调用getClass方法比较两者的类型
        System.out.println(l1.getClass() == l2.getClass()); // true
        List<String> list = new ArrayList<String>();
        // 由于系统中并未真正生成泛型类，所以instanceof后不能使用泛型类，下面代码编译报错Cannot perform instanceof check against parameterized type ArrayList<String>
        // if (list instanceof java.util.ArrayList<String>) {}
    }
}

class testStatic<T> {
    // static T name; // 错误，不能在静态变量声明中使用类型形参，Cannot make a static reference to the non-static type T

    T age;

    private void get(T grade) {
    }

    // private static void set(T grade) {} // 错误，不能在静态方法声明中使用类型形参，报错同上

}