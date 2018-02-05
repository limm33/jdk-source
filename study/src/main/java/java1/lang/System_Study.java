/*
 * 文件名：System_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： System_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月18日
 * 修改内容：新增
 */
package java1.lang;

import org.junit.Test;

/**
 * System.identityHashCode(obj)值可以唯一标示该对象，其根据对象地址得到，任何两个对象的identityHashCode值总是不同的。
 * 
 * @author yunhai
 */
public class System_Study {
    @Test
    public void print() {
        String str = "s";
        System.out.println(str.hashCode());
        System.out.println(System.identityHashCode(str));
    }
}
