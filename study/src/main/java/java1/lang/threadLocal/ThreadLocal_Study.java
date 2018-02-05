/*
 * 文件名：ThreadLocal_Study.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ThreadLocal_Study.java
 * 修改人：zxiaofan
 * 修改时间：2017年1月19日
 * 修改内容：新增
 */
package java1.lang.threadLocal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

import org.junit.Test;

/**
 * ThreadLocal源码分析相关代码csdn.zxiaofan.com.
 * 
 * @author zxiaofan
 */
public class ThreadLocal_Study {

    /**
     * ThreadLocal.
     */
    static ThreadLocal<String> threadLocal = null;

    @Test
    public void basicTest() {
        initThreadLocal();
        String result = threadLocal.get();
        System.out.println(result); // str1485166383408
        threadLocal.set("value_New");
        System.out.println(threadLocal.get()); // value_New
        threadLocal = new ThreadLocal<>(); // 没有重写initialValue方法，没有set
        System.out.println(threadLocal.get()); // null
    }

    /**
     * java8新特性.
     * 
     * java8的ThreadLocal新增<S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier)方法，允许使用Lambda表达式
     */
    @Test
    public void jdk8Test() {
        Supplier<String> supplier = new Supplier<String>() {

            @Override
            public String get() {
                return "supplier_new";
            }
        };
        threadLocal = ThreadLocal.withInitial(supplier);
        System.out.println(threadLocal.get()); // supplier_new
        threadLocal = ThreadLocal.withInitial(() -> "sup_new_2");
        System.out.println(threadLocal.get()); // sup_new_2
        ThreadLocal<DateFormat> localDate = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
        System.out.println(localDate.get().format(new Date())); // 2017-01-22
        ThreadLocal<String> local = new ThreadLocal<>().withInitial(supplier);
        System.out.println(local.get()); // supplier_new
    }

    /**
     * 初始化threadLocal(写这么麻烦主要是方便调试源代码).
     * 
     */
    private synchronized void initThreadLocal() {
        if (null == threadLocal) {
            threadLocal = new ThreadLocal<String>() {
                protected String initialValue() {
                    return "str" + System.currentTimeMillis();
                };
            };
        }

    }
}
