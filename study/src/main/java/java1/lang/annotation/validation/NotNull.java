package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 不能为null,可以作用到任意非基础类型对象
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    String scope() default "非基础类型"; // 作用域

    String paramLimit() default "不能是null"; // 参数限制
}
