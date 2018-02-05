/*
 * 文件名：Split_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Split_Study.java
 * 修改人：yunhai
 * 修改时间：2015年11月18日
 * 修改内容：新增
 */
package java1.lang.String1;

import java.text.MessageFormat;

import org.junit.Test;

/**
 * split本质是调用正则表达式。
 * 
 * regex参数表示按照regex来分割，分割结果不包含其本身。
 * 
 * @author yunhai
 */
public class Split_Study {

    @Test
    public void substring() {
        String N = "01:大汽车";
        String L = "";
        String R = "";
        int k = N.length();
        for (int i = 0; i < N.length(); i++) {
            if (N.substring(i, i + 1).equals(":")) {
                L = N.substring(0, i).trim();
                R = N.substring(i + 1, k).trim();
                System.out.println(L);
                System.out.println(R);
            }
        }
    }

    @Test
    public void split1() {
        String s = new String("01:大汽车");
        String[] a = s.split(":");
        System.out.println(a[0]);
        System.out.println(a[1]);
    }

    @Test
    public void split2() {
        String s = new String("01:大汽车:02:火车");
        String[] a = s.split(":", 2); // split第二个参数表示分割成多少个字符串
        System.out.println(a[0]); // output:01
        System.out.println(a[1]); // output:大汽车:02:火车
    }

    @Test
    public void split3() {
        String s = new String("01:大汽车:02:火车");
        System.out.println(s.split(":", 2)[0]); // output:01
        System.out.println(s.split(":", 2)[1]); // 将数组直接写到print而已，output:大汽车:02:火车
    }

    /**
     * split正则用法，待熟悉.
     * 
     */
    @Test
    public void splitRegex() {
        String str = "one且123q";
        String regex = "(且)"; // 按“且”分割，output：one、123q
        String[] strs = str.split(regex);
        for (int i = 0; i < strs.length; i++) {
            System.out.printf("strs[%d] = %s%n", i, strs[i]); // %d十进制
        }
        String regex2 = "(?<=one且)(?=123)"; // ？？？？？？？？？？
        String[] strs1 = str.split(regex2);
        for (int i = 0; i < strs1.length; i++) {
            System.out.printf("strs[%d] = %s%n", i, strs1[i]); // %d十进制
        }
    }

    @Test
    public void splitRegex2() {
        String str = "2011年10月2日日";
        String regex = "\\D{1,2}"; // 非数字，output：2011、10、2
        String[] strs = str.split(regex);
        for (int i = 0; i < strs.length; i++) {
            System.out.println(MessageFormat.format("strs({0}) = {1}", i, strs[i]));
        }
    }
}
