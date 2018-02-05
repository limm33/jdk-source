package other.lambda;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 文件名：asListStudy.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： asListStudy.java
 * 修改人：yunhai
 * 修改时间：2015年10月27日
 * 修改内容：新增
 */

/**
 * @author yunhai
 */
public class asListStudy {
    public static void main(String[] args) {
        testBasic();
        testOther();
    }

    private static void testBasic() {
        // (byte，short，char)--int--long--float--double;boolean
        /*
         * Arrays.asList中，其接收的参数原型是泛型参数，而基本类型是不能作为范型的参数;
         * 
         * 按道理应该使用包装类型，但这里却没有报错， 因为数组是可以泛型化的，所以转换后在list中就有一个类型为int的数组
         */
        System.out.println("基本类型演示");
        int[] arrInt = {1, 2, 3, 4};
        List<int[]> intList = Arrays.asList(arrInt); // intList包含了一个数组a，如何获取其中元素？？？？
        System.out.println("\tInt-size: " + intList.size() + "\tInt(0): " + intList.get(0) + "\tequals:" + intList.equals(arrInt) + "   " + intList.get(0).equals(arrInt));
        Integer[] arrInt1 = {1, 2, 3, 4}; // Integer不是基本类型
        List intList1 = Arrays.asList(arrInt1);
        System.out.println("\tInteger-size: " + intList1.size() + "\tInteger(0): " + intList1.get(0) + "\tequals:" + intList1.get(0).equals(arrInt1[0]));
        char[] arrch = {'q', 'w', 'e', 'r'};
        List charList = Arrays.asList(arrch);
        System.out.println("\tchar-size: " + charList.size() + "\tchar(0): " + charList.get(0));
        boolean[] boarr = {false, true};
        List boList = Arrays.asList(boarr);
        System.out.println("\tboolean-size: " + boList.size() + "\tboolean(0): " + boList.get(0));
    }

    private static void testOther() {
        // aslist不支持add、remove操作，报异常UnsupportedOperationException
        System.out.println("其他类型演示");
        String[] arr = {"aa", "bb", "cc", "dd"};
        List<String> list = Arrays.asList(arr);
        System.out.println("arr2-size: " + list.size());
        System.out.println(list.get(0).getClass());
        System.out.println(arr[0] == list.get(0));
        list.set(0, "newaa");
        System.out.println("修改 list--原arr的值：" + arr[0] + "\tlist的值：" + list.get(0));
        arr[2] = "newbb";
        System.out.println("修改原arr--原arr的值：" + arr[1] + "\tlist的值：" + list.get(1));
        // System.out.println("list执行add操作：");
        // list.add("ff");
        // System.out.println("list执行remove操作：");

        List<String> list22 = new ArrayList<String>(Arrays.asList(arr));
        list22.add("hh");
        System.out.println(list22.get(4));
        System.out.println(arr[4]);
    }
}