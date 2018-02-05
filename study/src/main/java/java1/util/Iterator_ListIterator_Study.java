package java1.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

/**
 * 【Iterator】 :hasNext、next、remove、forEachRemaining
 * 
 * 【ListIterator】:hasNext、next、remove、set、add、hasPrevious、previous、nextIndex、previousIndex、
 * 
 * ListIterator在Iterator接口基础上增加了向前迭代的功能，还可add、set元素。
 * 
 * ListIterator可指定迭代开始的位置，Iterator不可以。
 * 
 * @author yunhai
 */
public class Iterator_ListIterator_Study {
    @Test
    public void Iterator() {
        Collection col = new HashSet();
        col.add("hello");
        col.add("Java");
        col.add("Abstract");
        Iterator it = col.iterator();
        while (it.hasNext()) {
            System.out.println(it.next() + ""); // Java Abstract hello
            it.remove();
            // 迭代时不能【有效】修改集合元素，报异常ConcurrentModificationException
            // 如果HashSet包含的是对象，则对象不能修改，但对象内的属性可以修改。
            // 当向hashSet中添加可变对象时，如果修改其对象，有可能导致该对象与集合内其他对象相同，导致HashSet无法准确访问该对象
            col.remove("Java"); // 此行不会报错，因为it.remove()首先移除的是"Java"
            // col.remove("hello");
        }
        System.out.println(col.size()); // 0
    }

    /**
     * set的用法：只能在previous或next之后调用（得确定set的位置），不能在remove或add之后调用(IllegalStateException)。
     * 
     * set替换上一个被previous或next返回的元素的值.
     * 
     * previousIndex:返回即将previous的元素索引。
     */
    @Test
    public void ListIteratorTest() {
        List list = new ArrayList();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        ListIterator lIter = list.listIterator(4); // ListIterator可指定迭代开始的位置
        // lIter.set("s"); // IllegalStateException
        while (lIter.hasPrevious()) {
            // System.out.print(lIter.previousIndex() + " "); // 3 2 1 1 0【add("b")所以有两个1】
            Object object = (Object) lIter.previous();
            if (object.equals("B")) {
                lIter.add("b");
            }
            if (object.equals("A")) {
                lIter.set("a");
            }
            System.out.print(lIter.previousIndex() + " "); // 2 1 1 0 -1【如果ListIterator在begin位置则返回-1】
        }
        System.out.println();
        for (Object object : list) {
            System.out.print(object + " "); // a b B C D
        }
        System.out.println();
        lIter.remove();
        // lIter.add("add");
        // lIter.set("new-add"); // IllegalStateException
        for (Object object : list) {
            System.out.print(object + " "); // b B C D
        }
    }
}
