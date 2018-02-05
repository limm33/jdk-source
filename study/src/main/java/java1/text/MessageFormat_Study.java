package java1.text;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

public class MessageFormat_Study {
    public static void main(String[] args) {
        String str1 = "red";
        String str2 = "100cm";
        System.out.println(MessageFormat.format("玩具颜色是{0},高度{1}", str1, str2));// index从0开始

        String pattern = "{0}{0}{3}{4}{5}{6}{7}{8}{9}{10}"; // 缺1、2，输出缺BC；允许重复
        Object[] arr = new Object[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};// 第二个参数为数组，允许多传入参数
        String value = MessageFormat.format(pattern, arr);
        String val = MessageFormat.format(pattern, "a", 1, 3.1, 5f);// 第一个参数为模式字符串，允许多传入占位符，多传入的将原样输出
        System.out.println(value);
        System.out.println(val);

        System.out.println("-------单引号------");
        String mes = "{0} is 'a giant"; // 格式化字符串时，单个单引号(左单、右单、双单)会被省略
        Object[] array = new Object[]{"ZhangSan", 1.234, new Date()};
        String value1 = MessageFormat.format(mes, array);
        System.out.println(value1);
        String mes1 = "{0} is \\'a\\' giant"; // 是两个单引号不是双引号
        System.out.println(MessageFormat.format(mes1, array));
        String mes2 = "{0}' is a giant";// 左单引号'{0}或双单引号'{0}'使占位符保持原形
        System.out.println(MessageFormat.format(mes2, array));

        System.out.println("------占位符------");
        String mes3 = "'{0,number,#.#} is a giant";// 子格式模式，多了一个单引号
        System.out.println(MessageFormat.format(mes3, array));
        String mes4 = "{1,number,#.#} is a double"; // FormatType中没有String，unknown
                                                    // format type: string
        System.out.println(MessageFormat.format(mes4, array));
        System.out.println(MessageFormat.format("{2,time} test", array));
        System.out.println(MessageFormat.format("{2,date} test", array));
        System.out.println(MessageFormat.format("{2,,}", array));
        System.out.println(MessageFormat.format("{2,date,yyyy-MM-dd HH:mm:ss:ms}", array));
        System.out.println("------左右花括号------");
        String mes5 = "oh,'{' is a double";// 表示左花括号本身需'{'转义，但右花括号则不需要
        // String mes5 = "oh,{ is a double";//thread Unmatched braces in the
        // pattern.
        System.out.println(MessageFormat.format(mes5, array));

        System.out.println("------重复使用某个MessageFormat(多次格式同一个模式的字符串)------");
        Object[] array2 = new Object[]{"LiSi", 1.2};
        MessageFormat messageFormat = new MessageFormat(mes);
        System.out.println(messageFormat.format(array));
        System.out.println(messageFormat.format(array2));
        System.out.println("------格式化BigDecimal,先转为String------");
        BigDecimal decimal = new BigDecimal("1234");
        String param = "{0}asd";
        String res = MessageFormat.format(param, decimal);
        System.out.println(res); // 1,234asd
        System.out.println(MessageFormat.format(param, String.valueOf(decimal))); // 1234asd
    }
}
