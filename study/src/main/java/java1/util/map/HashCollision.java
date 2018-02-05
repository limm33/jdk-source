/*
 * 文件名：HashCollision.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： HashCollision.java
 * 修改人：yunhai
 * 修改时间：2015年11月20日
 * 修改内容：新增
 */
package java1.util.map;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 * Hash碰撞实例,HashMap和ConcurrentHashMap碰撞条件及链表转树的临界点是一样的.
 * 
 * @author yunhai
 */
public class HashCollision {

    private int MIN_TREEIFY_CAPACITY = 63;

    boolean bool = true; // true表示：baseCount=191时调试源码，观测扩容时，链表的具体变化。

    @Test
    public void test() {
        // HashMap<Country, String> map = new HashMap<Country, String>();
        ConcurrentHashMap<Country, String> map = new ConcurrentHashMap<HashCollision.Country, String>();
        hashCollision(map);
        System.out.println("--------即将树转链表--------");
        for (int i = 0; i < 6; i++) {
            map.remove(i * MIN_TREEIFY_CAPACITY);
        }
        System.out.println("--------查看table，发现   树转链表  失败  --------");
        System.out.println("--------HashMap、ConcurrentHashMap树转链表（树节点小于等于6），只会发生在扩容时！！！  --------");
    }

    private void hashCollision(Map<Country, String> map) {
        for (int i = 0; i < MIN_TREEIFY_CAPACITY; i++) {
            map.put(new Country(i, 1000 + i), "value" + i);
        }
        System.out.println("------开始Hash碰撞，生成链表------");
        for (int i = MIN_TREEIFY_CAPACITY; i < MIN_TREEIFY_CAPACITY * 2; i++) {
            map.put(new Country(i, 1000 + i), "value" + i);
        }
        System.out.println("------0桶位，即将到达链表转树临界点------");
        for (int i = MIN_TREEIFY_CAPACITY * 2; i < MIN_TREEIFY_CAPACITY * 8;) {
            map.put(new Country(i, 1000 + i), "value" + i);
            if (bool) {
                i += 1;
                if (i == 190 || i == 191) {
                    print(map);
                }
            } else {
                i += MIN_TREEIFY_CAPACITY;
            }

        }
        System.out.println(map.entrySet().size()); // 134=128+6,桶0有8个元素依旧没转树
        System.out.println("------0桶位，即将达到9个元素，链表转树------");
        for (int i = MIN_TREEIFY_CAPACITY * 8; i < MIN_TREEIFY_CAPACITY * 9;) { // 往桶0加一个元素
            map.put(new Country(i, 1000 + i), "value" + i);
            i += 64;
        }
        // print(map);

    }

    private void print(Map<Country, String> map) {
        Iterator<Country> iter = map.keySet().iterator();// put debug point at this line
        while (iter.hasNext()) {
            Country countryObj = iter.next();
            String capital = map.get(countryObj);
            System.out.println(countryObj.getName() + ";");
        }
        System.out.println("============");
    }

    /**
     * hashMap碰撞测试module。
     * 
     * @author yunhai
     *
     */
    public class Country {

        int name;

        long population;

        public Country(int name, long population) {
            super();
            this.name = name;
            this.population = population;
        }

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public long getPopulation() {
            return population;
        }

        public void setPopulation(long population) {
            this.population = population;
        }

        // If length of name in country object is even then return 31(any random number) and if odd then return 95(any random number).
        // This is not a good practice to generate hashcode as below method but I am doing so to give better and easy understanding of hashmap.
        @Override
        public int hashCode() {
            return this.name % MIN_TREEIFY_CAPACITY;
        }

        // @Override
        // public boolean equals(Object obj) {
        // Country other = (Country) obj;
        // if (name.equals(other.name))
        // return true;
        // return false;
        // }
    }
    // Country india = new Country("India", 1000); // Japan的Hash值是95，它的长度是奇数。
    // Country japan = new Country("Japan", 10000);
    //
    // Country france = new Country("France", 2000); // Russia的Hash值是31，它的长度是偶数。
    // Country russia = new Country("Russia", 20000);
    // Country china = new Country("China", 60000); // China的Hash值是66，它的长度是奇数。
    //
    // map.put(india, "Delhi");
    // map.put(japan, "Tokyo");
    // map.put(france, "Paris");
    // map.put(russia, "Moscow");
    // map.put(russia, "Moscow");
    // map.put(china, "BeiJing");
}
