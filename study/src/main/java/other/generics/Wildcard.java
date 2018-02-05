/*
 * 文件名：Wildcard.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Wildcard.java
 * 修改人：yunhai
 * 修改时间：2016年1月4日
 * 修改内容：新增
 */
package other.generics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Wildcard：类型通配符?
 * 
 * 若son是father的一个子类型（子类或子接口），G是具有泛型声明的类或接口，
 * 
 * G<son>并不是G<father>的子类型。但数组son[]依旧是father[]的子类型。
 * 
 * Java泛型设计原则，只要在编译时没有出现警告，就不会遇到ClassCastException异常。
 * 
 * 类型通配符？，如List<?>，？被称为通配符，其元素类型可匹配任何类型。
 * 
 * 被限制的类型通配符：List<? extends Shape>，表示所有Shape泛型List的父类。
 * 
 * 限制类型形参：
 * 
 * 类型通配符上限:public class Apple<T extends Number>{}
 * 
 * 类型通配符下限:public class Apple<T super Type>{}，表示必须是Type本身或他的父类。
 * 
 * 表示Apple泛型类的类型形参上限是Number类，即使用Apple类时T形参传入的实际参数只能是Number类或Number类的子类。Apple<String> apple=new Apple<>()将引起编译错误。
 * 
 * @author yunhai
 */
public class Wildcard {
    @Test
    public void testGenerics() {
        List<String> list = new ArrayList<>();
        Wildcard.method1(list); // output:0
        // 编译错误The method method(List<Object>) in the type Wildcard is not applicable for the arguments (List<String>)
        // Wildcard.method(list);
    }

    @Test
    public void testArray() {
        Integer[] ia = new Integer[5]; // 必须初始化容量或数组
        // Integer数组赋给Number数组，OK，但这是一种不安全的设计
        Number[] na = ia;
        System.out.println(na[0]); // output：null
        Number[] na1 = new Number[5];
        System.out.println(na1[0] = 0.5);// output：0.5
        // 下面代码编译正常，但运行引发ArrayStoreException异常
        na[0] = 0.5; // java.lang.ArrayStoreException: java.lang.Double
    }

    private void method(List<Object> c) {
        System.out.println(c.size());
    }

    private static void method1(List<String> c) {
        System.out.println(c.size());
    }
}
