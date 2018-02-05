/*
 * 文件名：LinkedHashMap_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： LinkedHashMap_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月30日
 * 修改内容：新增
 */
package java1.util.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import org.junit.Test;

/**
 * LinkedHashMap：HashMap+双向链表，允许key、value为null
 * 
 * 像hashMap一样用table储存元素【桶位依旧分散，和HashMap的存放位置相同】，put时直接调用的是HashMap的put方法；
 * 
 * 但LinkedHashMap的每个bucket都存了这个bucket的before和after，且每个before(after)又存储了自身的前驱后继，直到null；
 * 
 * @author yunhai
 */
public class LinkedHashMap_Study {

    /**
     * LinkedHashMap默认的遍历顺序是insertion order，如果想显式地指定为access order(最后一次访问的顺序，即把最近访问get的元素放到最后)【LRU算法：最近最少使用】，
     * 
     * 那么要将其构造方法的参数置为【true】(false 表示的是插入模式).
     * 
     * 显式地指定为access order后【这是前提】，调用get()方法，导致对应的entry移动到双向链表的最后位置，但是table的顺序没变。
     * 
     * 【So LinkedHashMap元素有序存放，但并不保证其迭代顺序一直不变】
     * 
     * 利用ArrayList的【ListIterator】可实现向前遍历
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void simpleTest() {
        LinkedHashMap map = new LinkedHashMap(16, (float) 0.75, true);
        map.put(null, 1);
        map.put(null, null); // 允许key、value为null，但key仅能有1个null
        map.put("6", "A");
        map.put("5", "B");
        map.put("33", "C");
        map.put("9", "C");

        Iterator<Map.Entry> iterl = map.entrySet().iterator();
        while (iterl.hasNext()) {
            Map.Entry entry = iterl.next();
            System.out.print(entry.toString() + "; "); // 6=A; 5=B; 33=C; 9=C;
        }
        System.out.println("\n利用ArrayList的【ListIterator】向前迭代");
        ListIterator<Map.Entry> iterpre = new ArrayList<Map.Entry>(map.entrySet()).listIterator(map.size());
        while (iterpre.hasPrevious()) {
            Map.Entry entry = iterpre.previous();
            System.out.print(entry.toString() + "; "); // 9=C; 33=C; 5=B; 6=A;
        }
        System.out.println("显式指定access order模式");
        map.get("5"); // 显式地指定为get读取模式后，调用get()方法，导致对应的entry移动到双向链表的最后位置，但是table的顺序没变。
        // Iterator<Map.Entry> iterget = map.entrySet().iterator();
        // while (iterget.hasNext()) {
        // Map.Entry entry = iterget.next();
        // System.out.println(entry.toString() + "; ");
        // }
        map.forEach((key, value) -> System.out.print(key + "->" + value + "; ")); // 6->A; 33->C; 9->C; 5->B;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void init() {
        Map map = new HashMap();
        map.put("a", 1);
        map.put("b", 2);
        LinkedHashMap lMap = new LinkedHashMap(map); // 用HashMap初始化LinkedHashMap。
        System.out.println(lMap);
        lMap.remove("a");
        lMap.put("a", 1);
        // remove后再put，集合结构变化：只要未冲突，table不改变（想想put原理就好理解了）；但链表改变，新元素始终在tail。
    }

    /**
     * Iteration比较：LinkedHashMap仅与size成比例，而HashMap则与capacity成比例【JDK源码】。
     * 
     * 即通常HashMap效率更高，但若HashMap的capacity远大于size，则其迭代效率低于LinkedHashMap。
     * 
     * sub=2，LinkedHashMap插入操作的效率接近HashMap的两倍，两种迭代差别不大。
     *
     * sub=8，LinkedHashMap插入操作的效率接近HashMap的1/2，Iterator迭代略快。
     * 
     * 数据量百万级：HashMap的ForEach遍历效率远高于LinkedHashMap，且当capacity/size越大，效果越明显；其他操作效率差别不大;
     * 
     * 数据量亿级：LinkedHashMap总体性能好于hashMap。
     * 
     * 【数据量亿级测试时，请勿同时测试两个Map，否则会内存溢出。】
     */
    final int sub = 31;

    final int num = 100_000_000;

    @Test
    public void efficientTest() {
        LinkedHashMap lMap = new LinkedHashMap();
        HashMap hMap = new HashMap();

        Long timehs = System.currentTimeMillis();
        for (int i = 1; i < num;) {
            hMap.put(i + "", i + "");
            i += sub;
        }
        System.out.println("HashMap【put】: " + (System.currentTimeMillis() - timehs) + " ms");
        Long timels = System.currentTimeMillis();
        // for (int i = 1; i < num;) {
        // lMap.put(i + "", i + "");
        // i += sub;
        // }
        System.out.println("LinkedHashMap【put】: " + (System.currentTimeMillis() - timels) + " ms");
        System.out.println("///////迭代性能测试///////////");
        Long itel = System.currentTimeMillis();
        Iterator iterl = lMap.entrySet().iterator();
        while (iterl.hasNext()) {
            Object object = (Object) iterl.next();
        }
        System.out.println("LinkedHashMap【Iterator】: " + (System.currentTimeMillis() - itel) + " ms");
        Long iteh = System.currentTimeMillis();
        Iterator iterh = hMap.entrySet().iterator();
        while (iterh.hasNext()) {
            Object object = (Object) iterh.next();
        }
        System.out.println("HashMap【Iterator】: " + (System.currentTimeMillis() - iteh) + " ms");
        // /////////////forEach
        Long forl = System.currentTimeMillis();
        lMap.forEach((key, value) -> System.out.print(""));
        System.out.println("LinkedHashMap【forEach】: " + (System.currentTimeMillis() - forl) + " ms");
        Long forh = System.currentTimeMillis();
        hMap.forEach((key, value) -> System.out.print(""));
        System.out.println("HashMap【forEach】: " + (System.currentTimeMillis() - forh) + " ms");
    }
}
