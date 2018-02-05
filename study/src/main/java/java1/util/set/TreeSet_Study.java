/*
 * 文件名：TreeSet_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TreeSet_Study.java
 * 修改人：yunhai
 * 修改时间：2016年3月7日
 * 修改内容：新增
 */
package java1.util.set;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * 
 * @author yunhai
 */
public class TreeSet_Study {
    /**
     * 默认使用元素的自然顺序对元素进行排序.
     */
    @Test
    public void testBasic() {
        Set<String> set = new TreeSet<String>();
        initSet(set);
        System.out.println(set); // [a, b, c, d, e, f]
    }

    /**
     * 树结构，无扩容一说.
     * 
     * 由构造函数可知：底层为TreeMap。public TreeSet() { this(new TreeMap<E,Object>()); }
     */
    @Test
    public void testResize() {
        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(i);
        }
        System.out.println(set);
    }

    private void initSet(Set<String> set) {
        set.add("f"); // 底层结构为NavigableMap，一种SortedMap
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        set.add("e");
        set.add("e"); // 无重复值
        // set.add(null); // 不允许值为null,NullPointerException
    }

    /**
     * 降序.
     * 
     */
    @Test
    public void testMyComparator() {
        Set<String> set = new TreeSet<String>(new MyComparator());
        initSet(set);
        System.out.println(set); // [f, e, d, c, b, a]
    }

    /**
     * 对某个类自定义规则排序.
     * 
     */
    @Test
    public void testMyPOJO() {
        Set<Person> set = new TreeSet<Person>(new PersonComparator());
        Person p1 = new Person(10);
        Person p2 = new Person(20);
        Person p3 = new Person(30);
        Person p4 = new Person(40);
        set.add(p1);
        set.add(p2);
        set.add(p3);
        set.add(p4);
        for (Iterator<Person> iterator = set.iterator(); iterator.hasNext();) {
            System.out.print(iterator.next() + " "); // 40 30 20 10
        }
    }

    class MyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);// 降序排列
        }
    }

    class PersonComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            return o2.score - o1.score; // 自定义规则按照分数降序排列
        }
    }

    class Person {
        int score;

        public Person(int score) {
            this.score = score;
        }

        public String toString() {
            return String.valueOf(this.score);
        }
    }
}
