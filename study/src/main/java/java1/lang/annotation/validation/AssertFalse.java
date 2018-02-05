package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan
 * 
 *         断言为false 该注解只能是boolean和Boolean使用
 * 
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertFalse {
    String scope() default "boolean或java.lang.Boolean"; // 作用域

    String paramLimit() default "只能为false"; // 参数限制
}
