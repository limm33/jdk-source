/*
 * 文件名：ConcurrentHashMap_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ConcurrentHashMap_Study.java
 * 修改人：yunhai
 * 修改时间：2016年1月7日
 * 修改内容：新增
 */
package java1.util.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 * ConcurrentHashMap
 * 
 * @author yunhai
 */
public class ConcurrentHashMap_Study {
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        Map<String, Integer> conMap = new ConcurrentHashMap(16, (float) 0.75, 50);
        // conMap.put("1", null); // key、value均不允许null，但可以""
        conMap.put("a", 1);
        conMap.put("b", 1);
        conMap.put("c", 1);
        conMap.put("d", 1);
        conMap.put("e", 1);
        conMap.get("a");
        conMap.remove("a", 1);
        System.out.println(conMap);
        for (Map.Entry<String, Integer> e : conMap.entrySet()) { // 用foreach迭代，Map定义时必须制定key-value类型，否则cant convert
            conMap.remove("d", 1); // 允许迭代时remove。。。废话
        }
        System.out.println(conMap);
    }
}
