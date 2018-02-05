/*
 * 文件名：HashSet_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： HashSet_Study.java
 * 修改人：yunhai
 * 修改时间：2016年3月7日
 * 修改内容：新增
 */
package java1.util.set;

import java.util.HashSet;

import org.junit.Test;

/**
 * HashSet Study
 * 
 * 底层是HashMap,扩容等存储结构都同HashMap。
 * 
 * HashMap中的Key是根据对象的hashCode() 和 euqals()来判断是否唯一的。
 * 
 * So：为了保证HashSet中的对象不会出现重复值，在被存放元素的类中必须要重写hashCode()和equals()这两个方法。
 * 
 * @author yunhai
 */
public class HashSet_Study {
    @Test
    public void hashSet_Study() {
        HashSet<Object> set = new HashSet<>();
        set.add(null); // permit null
        set.add(null); // but just one null
        set.add(2);
        set.add(2); // put方法的value传入伪值PRESENT = new Object()，仅仅为了保持映射关系
        set.remove(2);
        System.out.println(set.contains(null)); // 没有get方法
        set.forEach((s) -> System.out.print(s + "; "));
    }
}
