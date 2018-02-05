/*
 * 文件名：msToTime.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： msToTime.java
 * 修改人：yunhai
 * 修改时间：2015年11月19日
 * 修改内容：新增
 */
package java1.text.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * @author yunhai
 */
public class msToTime {
    long ms = 123456789L;

    // long ms = 1123456789L;

    /**
     * 毫秒转时分秒格式.
     * 
     */
    @Test
    public void msToTime() {
        System.out.println("======long型毫秒正确转换======");
        Integer ss = 1000; // 1s =1000 ms
        Integer mm = ss * 60;
        Integer hh = mm * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mm;
        Long second = (ms - day * dd - hour * hh - minute * mm) / ss;
        // Long milliSecond = ms - day * dd - hour * hh - minute * mm - second * ss;
        Long milliSecond = ms % 1000; // 两者皆可

        StringBuffer sb = new StringBuffer();
        sb.append(day + "天").append(hour + "小时").append(minute + "分").append(second + "秒").append(milliSecond + "毫秒");
        System.out.println(sb.toString()); // 1天10小时17分36秒789毫秒
    }

    @Test
    public void msToTime2() {
        System.out.println("======long型毫秒正确转换======");
        long SSS = ms % 1000;
        long ss = (ms - SSS) / 1000 % 60;
        long mm = (ms - SSS) / 1000 / 60 % 60;
        long HH = (ms - SSS) / 1000 / 60 / 60 % 24;
        long dd = (ms - SSS) / 1000 / 60 / 60 / 24;// 天数取余得考虑月份实际情况
        String result = dd + " " + HH + ":" + mm + ":" + ss + "." + SSS;
        System.out.println(result); // 1 10:17:36:789
    }

    /**
     * long直接格式化为date，结果>=1970年，比实际多1月1天8小时.时间戳则正常转换。
     * 
     */
    @Test
    public void msToTime3() {
        System.out.println("======msToTime3======");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY:MM:dd HH:mm:ss:S");
        Date date = new Date(ms);
        // 多了1天8小时，如果天数较大，格式dd将舍弃月份】128 17:50:56:789-->10 01:50:56:789(130天即4月10天)
        System.out.println(sdf.format(date)); // 02 18:17:36:789 【

        long l = System.currentTimeMillis();
        System.out.println(l); // 1447935184881
        Date date2 = new Date(l);
        System.out.println(sdf.format(l)); // 19 20:13:04:881 【正常输出当前时间】
    }
}
