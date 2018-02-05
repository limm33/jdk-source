package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringLength {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String scope() default "java.lang.String"; // 作用域

    String paramLimit() default "长度不在区间范围内"; // 参数限制
}
