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
public @interface Number {
    double min() default Double.MIN_VALUE;

    double max() default Double.MAX_VALUE;

    String defaultValue() default "0";

    // 不在最大值最小值之间取这个值.
    String overstep() default "0";

    String scope() default "只能使用在byte/short/int/long/double/float等类型"; // 作用域

    String paramLimit() default ""; // 参数限制

}
