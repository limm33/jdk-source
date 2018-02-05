package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号校验,只能作用到String
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String scope() default "java.lang.String"; // 作用域

    String regex = "^[1][3-8]+\\d{9}$|^0\\d{2,3}-\\d{5,9}$|0\\d{7,12}$";

    String paramLimit() default "必须为手机号，格式：" + regex; // 参数限制
}
