/*
 * 文件名：Example1.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Example1.java
 * 修改人：yunhai
 * 修改时间：2015年10月27日
 * 修改内容：新增
 */

/**
 * @author     yunhai
 */

package other.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Lambda1 {
    public static void main(String[] args) {
        String[] atp = {"aa", "bb", "cc", "dd"};
        List<String> players = Arrays.asList(atp);// 陷阱
        // Stream streamArr = Arrays.stream(atp);// 数组转为流
        Stream<String> streamArr = Stream.of(atp);// 数组转为流方式2
        streamArr.filter(p -> p == "cc").forEach(p -> System.out.println("Array-Stream： " + p));
        // Old
        for (String player : players) {
            System.out.print(player + "; ");
        }
        System.out.println("");

        // lambda expression
        players.forEach((player) -> System.out.print(player + "; "));
        System.out.println();
        // List<Object>中参数类型不统一，则Lambda无法获取属性值(无法consumer.getName())

        // Using double colon operator in Java 8
        players.forEach(System.out::println);
        System.out.println("");

        // Using anonymous innerclass匿名的内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world ! anonymous");
            }
        }).start();

        // Using lambda expression
        new Thread(() -> System.out.println("Hello world ! lambda")).start();

        // Using anonymous innerclass
        Runnable race1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world ! r1");
            }
        };

        // Using lambda expression
        Runnable race2 = () -> System.out.println("Hello world ! r2");
        // 直接调用 run 方法(没开新线程!)
        race1.run();
        race2.run();
    }

}
