/*
 * 文件名：LocalDate_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： LocalDate_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月28日
 * 修改内容：新增
 */
package java1.text.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Java8
 * 
 * @author yunhai
 */
public class LocalDate_Study {
    @Test
    public void formatAndparse() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy--MM-dd");
        // LocalDate localDate = LocalDate.of(2015, 1, 5); // 直接生成LocalDate
        LocalDate localDate = LocalDate.of(2015, Month.JANUARY, 5); // 直接生成LocalDate
        System.out.println(localDate.format(dtf)); // 2015--01-05
        System.out.println(LocalDate.now().format(dtf)); // 2015--12-28
        String strDate = "2015--02-05";
        System.out.println(LocalDate.parse(strDate, dtf)); // 2015-02-05
        System.out.println(LocalDate.parse("2015-03-03")); // 2015-03-03,必须ISO_LOCAL_DATE即4-2-2,否则java.time.format.DateTimeParseException
    }

    @Test
    public void With() {
        LocalDate today = LocalDate.now();
        // 本月第1天
        System.out.println(today.with(TemporalAdjusters.firstDayOfMonth()));
        // 本月第一个周日
        System.out.println(today.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY)));
        // LocalDate的下一个周日
        System.out.println(today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
        // 两个日期间的天数
        long between = ChronoUnit.DAYS.between(LocalDate.parse("2015-12-27"), today);
        System.out.println(between);
        System.out.println(ChronoUnit.MONTHS.between(LocalDate.parse("2015-03-28"), today)); // 【9】03-28到12-28大于等于9个月
        System.out.println(ChronoUnit.MONTHS.between(LocalDate.parse("2015-03-29"), today)); // 【8】03-29到12-28小于9个月
    }

    /**
     * LocalDate、SimpleDateFormat生成指定范围的日期list.
     * 
     */
    @Test
    public void getRangeList() {
        String timeStart = "2015-01-29";
        String timeEnd = "2015-03-01";
        System.out.println(getTimeRangeListLocal(timeStart, timeEnd));
        try {
            System.err.println(getTimeRangeListSimple(timeStart, timeEnd));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * LocalDate生成指定范围的日期list.
     * 
     * 包含首尾日期。
     */
    private List<LocalDate> getTimeRangeListLocal(String timeStart, String timeEnd) {
        LocalDate lDateStart = LocalDate.parse(timeStart);
        LocalDate lDateEnd = LocalDate.parse(timeEnd);
        List<LocalDate> list = new ArrayList<LocalDate>();
        while (lDateStart.isBefore(lDateEnd)) {
            list.add(lDateStart);
            lDateStart = lDateStart.plusDays(1);
        }
        list.add(lDateStart); // add截止日期
        return list;
    }

    /**
     * SimpleDateFormat获取一个时间段的日期String的链表.
     * 
     * 包含首尾日期。
     */
    private List<String> getTimeRangeListSimple(String beginStr, String endStr) throws ParseException {
        List<String> result = new LinkedList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long beginTime = format.parse(beginStr).getTime();
        long endTime = format.parse(endStr).getTime();
        int spanTime = 60 * 60 * 24 * 1000;
        while (beginTime <= endTime) {
            result.add(format.format(new Date(beginTime)));
            beginTime += spanTime;
        }
        return result;
    }
}
