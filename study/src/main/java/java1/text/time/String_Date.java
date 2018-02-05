/*
 * 文件名：String_Date.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： String_Date.java
 * 修改人：yunhai
 * 修改时间：2015年11月19日
 * 修改内容：新增
 */
package java1.text.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * @author yunhai
 */
public class String_Date {

    @Test
    public void stringToDate() {
        System.out.println("======stringToDate======");
        String str = "13 0:4:16:789";
        SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss:S");
        try {
            Date date = sdf.parse(str);
            System.out.println(date); // Tue Jan 13 00:04:16 CST 1970 ,缺失的年月默认补1970年1月
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateString = "2012-12-06 ";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
            Date date = sdf1.parse(dateString);
            System.out.println(date); // Thu Dec 06 00:00:00 CST 2012,缺失的时分秒默认补0
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void dateToString() {
        System.out.println("======dateToString======");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM:dd HH:mm:ss:S");
        System.out.println(sdf.format(date));
    }
}
