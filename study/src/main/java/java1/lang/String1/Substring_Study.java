/*
 * 文件名：Substring_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Substring_Study.java
 * 修改人：yunhai
 * 修改时间：2015年11月11日
 * 修改内容：新增
 */
package java1.lang.String1;

/**
 * 检查参数合法性(正数，小于等于length，后>前)，含前不含后.
 * 
 * @author yunhai
 */
public class Substring_Study {
    public static void main(String[] args) {
        String str = "abcdefg";
        // System.out.println(str.substring(-3)); //参数不合法
        System.out.println(str.substring(3));
        // System.out.println(str.substring(3, 10));// 参数不合法
        // System.out.println(str.substr); // Java没有substr，C++的函数
    }
}
