package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 不能为null或者空字符串,只能作用在String上
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullAndEmpty {
    String scope() default "java.lang.String"; // 作用域

    String paramLimit() default "不能为null或者空字符串"; // 参数限制
}
