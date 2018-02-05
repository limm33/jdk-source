/*
 * 文件名：GenericsMethod.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： GenericsMethod.java
 * 修改人：yunhai
 * 修改时间：2016年1月4日
 * 修改内容：新增
 */
package other.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * GenericsMethod：泛型方法（Java5），声明时定义一个或多个类型形参。
 * 
 * 通配符适用于：支持灵活的子类化；可定义形参或变量的类型；
 * 
 * 泛型适用于：参数间或方法返回值与参数间的类型依赖关系；类型形参必须在对应方法中显示声明。
 * 
 * 若程序显示指定泛型构造器中实际形参则不可以用“菱形”语法。
 * 
 * Java不支持泛型数组。应通过instanceof关键字保证数据类型。
 * 
 * // 实现将一个Object数组元素复制到Collection集合的方法。
 * 
 * @author yunhai
 */
public class GenericMethod {
    // 只能将Object数组元素复制到Object的Collection集合中，Object的子类都不行。
    // 换成通配符Collection<?>是否可行呢？显然是不行的，Java不允许将对象放到未知类型的集合中,col.add(object)将报错。
    private static void arrayToCol(Object[] from, Collection<Object> col) {
        for (Object object : from) {
            col.add(object);
        }
    }

    /**
     * 泛型方法.
     * 
     * 接口、类声明中定义的形参可以在整个接口、类中使用；但方法声明中定义的形参只能在该方法里使用。
     */
    static <T> void arrayToColGeneric(T[] from, Collection<T> col) {
        for (T object : from) {
            col.add(object);
        }
    }

    @Test
    public void test() {
        // 只能将Object数组元素复制到Object的Collection集合
        Object PHP = "PHP";
        Object[] obj = {PHP};
        List<Object> listObj = new ArrayList();
        arrayToCol(obj, listObj);
        System.out.println(listObj); // [PHP]
        // 通过COllection<Object>将元素复制到String的Collection集合，编译错误
        String[] arr = {"Java", "C++"};
        List<String> list = new ArrayList<String>();
        // arrayToCol(arr, list); // 编译错误 ， Collection<String>不是COllection<Object>的子类。
        // 泛型方法
        arrayToColGeneric(arr, list);
        System.out.println(list); // [Java, C++]
        Integer[] ia = new Integer[3];
        Collection<Number> cn = new ArrayList<>();
        arrayToColGeneric(ia, cn);
        System.out.println(cn); // [null, null, null]
    }

    @Test
    public void test2() {
        List<Object> o = new ArrayList<>();
        List<String> s = new ArrayList<String>();
        // colToColError(s, o); // 编译错误，(Collection<T>, Collection<T>) is not applicable for the arguments (List<String>, List<Object>)
        colToColRight(s, o); // 正确
        // 显示指定构造器中声明的T形参是Integer类型，则无法使用菱形语法。
        // MyClass<String> mcClass = new<Integer> MyClass<>(5); // 编译错误
        // MyClass<Integer> mcClass1 = new<Integer> MyClass<>(5);
        MyClass<Integer> mcClass2 = new<Integer> MyClass<Integer>(5);
    }

    /**
     * 此处要求实参泛型类型必须相同.
     */
    static <T> void colToColError(Collection<T> from, Collection<T> to) {
        for (T t : from) {
            to.add(t);
        }
    }

    /**
     * 类型通配符+泛型.
     */
    static <T> void colToColRight(Collection<? extends T> from, Collection<T> to) {
        for (T t : from) {
            to.add(t);
        }
    }
}

class MyClass<E> {
    public <T> MyClass(T t) {
        System.out.println(t);
    }
}
