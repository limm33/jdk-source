package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 必须是将来某个时刻,该注解只能是java.util.Date/long/java.long.Long/java.sql.Timestamp
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureTime {
    String scope() default "java.util.Date"; // 作用域

    String paramLimit() default "必须是未来某个时间"; // 参数限制
}
