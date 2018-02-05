/*
 * 文件名：BaseAnno.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： BaseAnno.java
 * 修改人：zxiaofan
 * 修改时间：2016年11月10日
 * 修改内容：新增
 */
package java1.lang.annotation.validation;

import java.lang.annotation.Inherited;

/**
 * 其他注解尽量实现此注解的方法.
 * 
 * @author zxiaofan
 */

@Inherited // 可被继承
public @interface BaseAnno {
    String scope() default ""; // 作用域

    String paramLimit() default ""; // 参数限制
}
