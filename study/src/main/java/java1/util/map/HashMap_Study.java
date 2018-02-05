/*
 * 文件名：HashMap_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： HashMap_Test.java
 * 修改人：yunhai
 * 修改时间：2015年11月9日
 * 修改内容：新增
 */
package java1.util.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

/**
 * @author yunhai
 */
public class HashMap_Study {
    static int MAXIMUM_CAPACITY = 1 << 30;

    @Test
    public void main() {
        HashMap<Object, Object> map = new HashMap<>();// 如果是(?,?)，仅(null,null)不报错
        map.put(null, null); // 允许null=null
        map.put(null, "1"); // 1覆盖null
        map.put("2", null);
        map.put("3", null);
        map.put("33", 123);
        System.out.println("======key、value均允许null，但只允许有一个key为null(覆盖)======");
        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put(9, 123);
        map2.put("2", 8);
        System.out.println(map.get("33").equals(map2.get(9)));
        map.putAll(map2); // putAll可以合并map，如果key重复，则用新值替换就值
        map.putIfAbsent("33", 7); // 检测指定的key对应的value是否为null，如果该key对应的value为null，则使用传入的新value代替原来的null。
        print(map);
        System.out.println(map.toString());
        System.out.println(map.equals(map2));
    }

    /**
     * replace_Test.
     */
    @Test
    public void replace_Test() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(3, 3);
        map.put(6, 6);
        map.put(9, 9);
        map.put(63, 63);
        map.replace(3, 5);
        print(map);
        map.replace(3, 3, 3); // (Object key, Object oldValue, Object newValue)
        print(map);
        map.replace(3, 5, 3);
        print(map);
        map.replaceAll((key, value) -> {// 其他用法？？？
            if ((int) key > 6) {
                value = 99;
            }
            return value; // value改变，返回value
        });
        print(map);
    }

    /**
     * remove、clear.
     */
    @Test
    public void remove_clear_Test() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(null, "1");
        map.put("2", null);
        map.put(3, 6);
        map.remove("2");
        map.remove("2", null); // 允许remove不存在的元素
        print(map);
        map.clear();
        // map = null; // 将不能对map任何操作
        System.out.println(map.isEmpty());
        System.out.println("clear后size:" + map.size()); // output:0
    }

    /**
     * get、containsKey、containsValue.
     */
    @Test
    public void get_contains_Test() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(null, "1");
        map.put("2", null);
        map.put(3, 6);
        System.out.println(map.get(3));
        System.out.println(map.getOrDefault(3, "NoKey"));
        System.out.println(map.getOrDefault("3", "NoKey"));
        System.out.println(map.containsKey(null));
        System.out.println(map.containsValue("1"));
    }

    /**
     * 遍历方式.
     */
    @Test
    public void iteration_Test() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, 2);
        map.put(8, 4);
        map.put(6, "s");
        map.forEach((key, value) -> System.out.println(key + "=" + value));// 自带lambda遍历
        // map.forEach((key) -> System.out.println(key)); //报错，参数必须同时包含key和value

        // 迭代时成功remove存在的元素，如1,才会ConcurrentModificationException.
        /*
         * map.forEach((key, value) -> { System.out.println(key + "==" + value); map.remove(1); });
         */

        System.out.println("======1.7遍历方式entrySet/keySet/values,1.8保留======");
        for (Entry<Object, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            // entry.remove(); // error
        }
        System.out.println("======迭代时Remove======");
        Iterator<Entry<Object, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            Integer key = (Integer) entry.getKey();
            if (key % 2 == 0) {
                System.out.println("Delete key：" + key);
                it.remove();
                System.out.println("The key " + +key + " was deleted");

            }
        }
    }

    /**
     * HashMap的正确迭代方式.
     * 
     */
    @Test
    public void CorrectIterator() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(3, 4);
        Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, Integer> entry = it.next();
            Integer key = (Integer) entry.getKey();
            if (key % 2 == 0) {
                System.out.println("Delete key：" + key);
                it.remove(); // 从集合中删除上一次next返回的元素
                System.out.println("The key " + +key + " was deleted");
            }
        }
    }

    @Test
    public void RemoveConcurrentModificationException() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(3, 4);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.remove(1); // ConcurrentModificationException
            // 如果HashMap中仅有一个键值对（1,2），map.entrySet()下则不会抛异常。因为remove本身不会并不会抛异常，异常在于remove之后modCount与expectedModCount不相等
        }
        /*
         * lambda表达式迭代，即使仅有一组键值对，依旧会抛异常。
         * 
         * map.forEach((key, value) -> { System.out.println(key + "==" + value); map.remove(1); });
         */
    }

    @Test
    public void tableSizeFor_Test() {
        int cap = 97;
        System.out.println(tableSizeFor(cap));
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        System.out.println(Integer.toBinaryString(n));
        n |= n >>> 1; // |=有1为1
        System.out.println(n);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;
        System.out.println(n);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    private void print(HashMap<Object, Object> map) {
        map.forEach((key, value) -> System.out.print(key + "=" + value + ", "));
        System.out.println();
    }

}
