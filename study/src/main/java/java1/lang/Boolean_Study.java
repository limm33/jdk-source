/*
 * 文件名：Boolean_Study.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Boolean_Study.java
 * 修改人：zxiaofan
 * 修改时间：2016年8月31日
 * 修改内容：新增
 */
package java1.lang;

import org.junit.Test;

/**
 * Boolean.getBoolean("true")的那些坑
 * 
 * @author zxiaofan
 */
public class Boolean_Study {
    @Test
    public void testGet() {
        System.out.println(Boolean.getBoolean("true")); // false
        //
        System.out.println(Boolean.parseBoolean("true")); // true
        System.out.println(Boolean.parseBoolean("True")); // true
        System.out.println(Boolean.valueOf("true")); // true
        System.out.println(Boolean.valueOf("True")); // true
        // getBoolean仅当参数为系统属性且为true时返回true
        String systemProperty = "true";
        System.setProperty("key", systemProperty); // key-value
        System.out.println(Boolean.getBoolean("key"));
    }
}
