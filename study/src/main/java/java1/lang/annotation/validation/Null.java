package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 只能为null,可以作用到任意非基础类型对象
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Null {
    String scope() default "非基础类型"; // 作用域

    String paramLimit() default "必须是null"; // 参数限制
}
