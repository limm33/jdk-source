package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 只能作用在String上
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tel {
    String scope() default "java.lang.String"; // 作用域

    String regex = "^0\\d{2,3}-{0,1}\\d{5,9}$|0\\d{7,12}$";

    String paramLimit() default "必须为座机，格式："; // 参数限制
}
