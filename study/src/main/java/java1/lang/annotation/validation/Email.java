package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan 邮件地址,该注解只能String使用
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    // String regex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
    String regex_en = "[a-zA-Z0-9][a-zA-Z0-9_\\-\\.]{0,19}@(?:[a-zA-Z0-9\\-]+\\.)+[a-zA-Z]+";

    // 一-龥（yu不是乱码） 指所有的汉字
    String regex_en_zn = "[\u4e00-\u9fa5a-zA-Z0-9][\u4e00-\u9fa5_a-zA-Z0-9\\-\\.]{0,19}@(?:[\u4e00-\u9fa5a-zA-Z0-9\\-]+\\.)+[\u4e00-\u9fa5a-zA-Z]+";

    String regex = "^" + regex_en_zn + "$";

    String scope() default "java.lang.String"; // 作用域

    String paramLimit() default "必须是邮件地址,格式："; // 参数限制
}
