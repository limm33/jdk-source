/*
 * 文件名：GsonToMap.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： GsonToMap.java
 * 修改人：zxiaofan
 * 修改时间：2016年7月22日
 * 修改内容：新增
 */
package java1.util.map;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson将json转Map的那些坑.
 * 
 * 最新版2.7依旧有问题.
 * 
 * @author zxiaofan
 */
@SuppressWarnings("unchecked")
public class GsonToMap {
    String json1 = "{\"k1\"=\"外观\",\"k2\"=\"大厅\"}";

    String json2 = "{0=\"外观\",1=\"大厅\"}";

    Gson gson = new Gson();

    /**
     * Gson能正常转json为Map.
     * 
     * Map的key允许Integer
     * 
     */
    @Test
    public void test() {
        Map<String, String> map = gson.fromJson(json1, HashMap.class);
        System.out.println(map.get("k1")); // 输出“外观”
        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "value");
        System.out.println(map2.get(1)); // 输出“value”
    }

    /**
     * Gson转json为Map显现bug.
     * 
     * 原因：Gson将json转为Map时，将Integer类型的key存成了String类型，导致key的hash值不等于原来Integer型的hash值。
     * 
     * 实际上更应理解为Gson在转换json时，根本不知道HashMap内部key是Integer类型的，reflect确定类型后转换一切正常。
     * 
     * 
     */
    @Test
    public void testBug() {
        System.out.println("===Gson不知Map键值对的类型===");
        Map<Integer, String> map = gson.fromJson(json2, HashMap.class);
        System.out.println(map.get(new Integer("1"))); // 输出 null
        System.out.println(map.get("1")); // 输出 "大厅"

        System.out.println("===通过reflect告知Gson Map键值对的类型===");
        java.lang.reflect.Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        Map<Integer, String> map2 = gson.fromJson(json2, type);
        System.out.println(map2.get(new Integer("1"))); // 输出 "大厅"
        System.out.println(map2.get("1")); // 输出 null
        // reflect确定类型后转换一切正常
    }
}
