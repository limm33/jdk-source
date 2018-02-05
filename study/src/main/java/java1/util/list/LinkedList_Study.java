/*
 * 文件名：LinkedList_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： LinkedList_Study.java
 * 修改人：xiaofan
 * 修改时间：2016年3月27日
 * 修改内容：新增
 */
package java1.util.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * @author xiaofan
 */
public class LinkedList_Study {
    @Test
    public void test() {
        List list = new LinkedList<>();
        list.add(null);
        list.add(null); // 允许null，且允许重复
        System.out.println(list); // [null, null]
        // contains调用int indexOf()方法，先判断入参是否是null，是则x.item == null，不是则equals比较，最后返回index或-1
        System.out.println(list.contains(null)); // true
        list.remove(null); // 只remove第一个元素
        list.add(1);
        list.remove((Integer) 1); // remove int本身
        // list.remove(Integer.valueOf(1)); // remove int本身，入参为包装类Integer
        int i = 0;
        // remove指定位置元素，调用E remove(int index)方法，必须直接传入int型的变量
        list.remove(i);
        list.add("a");
        list.add("b");
        Iterator ite = list.iterator();
        while (ite.hasNext()) {
            Object object = (Object) ite.next();
            System.out.println(object);
            list.remove("a"); // 迭代时remove抛ConcurrentModificationException
        }
    }

    /**
     * 迭代时remove不抛ConcurrentModificationException特例【实际元素个数<=2】.
     * 
     * 和HashMap一样，迭代时remove本身并不会抛异常。
     * 
     * 此处异常在于ite.next()方法会执行checkForComodification来检测(modCount != expectedModCount)
     */
    @Test
    public void ConcurrentModificationException() {
        List list = new LinkedList<>();
        list.add("a");
        list.add("b");
        Iterator ite = list.iterator();
        while (ite.hasNext()) { // hasNext() {return nextIndex < size;}
            // 当list只有2个元素，remove第一个后，nextIndex=1=size，hasNext()返回false，也就不会再执行ite.next()，也就不会抛异常了
            Object object = (Object) ite.next();
            System.out.println(object);
            list.remove("b");
        }
    }
}
