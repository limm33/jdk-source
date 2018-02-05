/*
 * 文件名：DateTimeFormatter_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： DateTimeFormatter_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月24日
 * 修改内容：新增
 */
package java1.text.time.format;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.junit.Test;

/**
 * DateTimeFormatter（since1.8）： immutable and thread-safe【线程安全】
 * 
 * LocalDateTime.parse默认省略时间的0秒；
 * 
 * @author yunhai
 */
public class DateTimeFormatter_Study {
    String strDate = "2015--12-24 09:00:00";

    String strDate1 = "2015--12-24 09:00:12";

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy--MM-dd HH:mm:ss");

    DateTimeFormatter[] dtfs = new DateTimeFormatter[]{

    DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_LOCAL_DATE,

    DateTimeFormatter.ISO_LOCAL_TIME, DateTimeFormatter.ISO_DATE_TIME,

    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM),

    dtf

    };

    @Test
    public void format() {
        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < dtfs.length; i++) {
            System.out.println(date.format(dtfs[i]));
            System.out.println(dtfs[i].format(date));
        }
    }

    @Test
    public void parse() {
        LocalDateTime ldTime = LocalDateTime.parse(strDate, dtf); // 2015-12-24T09:00
        System.out.println(ldTime);
        System.out.println(LocalDateTime.parse(strDate1, dtf)); // 2015-12-24T09:00:12
        // 默认parse时，格式必须4-2-2，否则java.time.format.DateTimeParseException
        System.out.println(LocalDate.parse("214-02-28")); // 严格按照ISO yyyy-MM-dd验证(4-2-2)，02写成2都不行;当然可以重载方法自己定义格式
    }
}
