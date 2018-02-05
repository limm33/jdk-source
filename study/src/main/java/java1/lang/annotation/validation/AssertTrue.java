package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 断言为true 该注解这能是boolean和Boolean使用
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertTrue {
    String scope() default "boolean或java.lang.Boolean"; // 作用域

    String paramLimit() default "只能为true"; // 参数限制
}
