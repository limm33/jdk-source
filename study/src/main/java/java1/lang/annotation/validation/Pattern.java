package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 正则表达式,只能作用到String上
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String value();

    String scope() default "java.lang.String"; // 作用域

    String paramLimit() default "必须满足格式："; // 参数限制
}
