/*
 * 文件名：EnumMap_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： EnumMap_Study.java
 * 修改人：yunhai
 * 修改时间：2016年3月9日
 * 修改内容：新增
 */
package java1.util.map;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/**
 * 
 * @author yunhai
 */
enum Season {
    spring, summer, fail, winter
}

public class EnumMap_Study {
    /**
     * 基础用法、特性.
     * 
     */
    @Test
    public void basic() {
        EnumMap<Season, Object> enumMap = new EnumMap<>(Season.class); // 创建时必须指定key的枚举类
        enumMap.put(Season.summer, null); // value可以为null
        System.out.println(enumMap.put(Season.spring, "春暖花开")); // key为枚举，value为object
        // enumMap.put(null, "zxiaofan.cn"); // NullPointerException
        System.out.println(enumMap.get(Season.spring));
        System.out.println(enumMap.containsKey((Season.spring)));
        System.out.println(enumMap.containsValue("春暖花开"));
        System.out.println(enumMap);
    }

    @Test
    public void iterator() {
        EnumMap<Season, Object> enumMap = initEnumMap();
        // 遍历KeySet
        Set keySet = enumMap.keySet();
        Iterator iteKey = keySet.iterator();
        while (iteKey.hasNext()) {
            Object object = (Object) iteKey.next();
            System.out.print(object + "=" + enumMap.get(object) + "; ");
        }
        System.out.println();
        // 遍历values
        Collection<Object> vals = enumMap.values();
        Iterator iteVal = vals.iterator();
        while (iteVal.hasNext()) {
            Object object = (Object) iteVal.next();
            System.out.print(object + "; ");
            // ((Iterator) object).remove();// ClassCastException: String cannot be cast to Iterator
        }
        System.out.println();
        // 遍历Entry
        Set<Entry<Season, Object>> entrySet = enumMap.entrySet();
        Iterator iteEn = entrySet.iterator();
        while (iteEn.hasNext()) {
            Entry object = (Entry) iteEn.next();
            System.out.print(object.getKey() + "; ");
            iteEn.remove();
        }
        System.out.println();
        System.out.println(enumMap); // {}

    }

    private EnumMap<Season, Object> initEnumMap() {
        EnumMap<Season, Object> enumMap = new EnumMap<>(Season.class); // 创建时必须指定key的枚举类
        enumMap.put(Season.summer, null);
        enumMap.put(Season.spring, "春暖花开");
        enumMap.put(Season.winter, "万里飘雪");
        return enumMap;
    }

    @Test
    public void ConcurrentModificationException() {
        EnumMap<Season, Object> enumMap = initEnumMap();
        for (Object entry : enumMap.entrySet()) {
            enumMap.remove(Season.summer); // 【--不报--异常】ConcurrentModificationException
        }
        System.out.println(enumMap); // {}
        // 和HashMap对比
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        // map.put(3, 4);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.remove(1); // ConcurrentModificationException
            // 如果HashMap中仅有一个键值对（1,2），则不会抛异常（但Lambda表达式跌打依旧会抛异常）。因为remove本身不会并不会抛异常，异常在于remove之后modCount与expectedModCount不相等
        }
        map.forEach((key, value) -> {
            System.out.println(key + "==" + value);
            // map.remove(1); // ConcurrentModificationException
        });
    }
}
