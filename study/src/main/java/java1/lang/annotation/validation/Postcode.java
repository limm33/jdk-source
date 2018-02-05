package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 只能作用在Sting上
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Postcode {
    String scope() default "java.lang.String"; // 作用域

    String paramLimit() default "必须为邮编，格式：" + regex; // 参数限制

    String regex = "^\\d{6}$";
}
