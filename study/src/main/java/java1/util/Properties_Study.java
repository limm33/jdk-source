/*
 * 文件名：Properties_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Properties_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月31日
 * 修改内容：新增
 */
package java1.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

/**
 * pros extends Hashtable
 * 
 * @author yunhai
 */
public class Properties_Study {
    String path = "D:\\pros.ini";

    @Test
    public void readAndWrite() throws FileNotFoundException, IOException {
        Properties pros = new Properties();
        pros.put("name", "Jack"); // put插入元素
        pros.put("pwd", "pwd123");
        pros.store(new FileOutputStream(path), "MyProperties-Test"); // 写入

        Properties pros2 = new Properties();
        pros2.setProperty("grade", "99"); // setProperty插入元素
        pros2.store(new FileOutputStream(path, true), "add-Test"); // 追加

        Properties pros3 = new Properties();
        pros3.put("read", "read-yes");
        pros3.load(new FileInputStream(path)); // 从Properties读取key-value对，并添加到pros3;读取时自动忽略其中的注释
        System.out.println(pros3);
        pros3.store(new FileOutputStream("D:\\pros3.ini"), "load key-value,reWrite .ini"); // 将新数据重新写入ini
        System.out.println(pros3.get("name")); // get方法
        pros3.storeToXML(new FileOutputStream("D:\\pros.xml"), "store to xml", "UTF-8"); // storeToXML
    }

    /**
     * 清空properties文件
     * 
     * clear无效，原因未知，只有暂时删除文件再new一个
     * 
     * @throws IOException
     * @throws FileNotFoundException
     *
     */
    @Test
    public void clear() {
        String s = path;// 文件的绝对路径
        File file = new File(s);
        if (file.exists()) {
            boolean d = file.delete();
            if (d) {
            } else {
                System.out.print("删除失败！");
            }
        }
        Properties pros = new Properties();
        // pros.put("read", "read-yes");
        try {
            pros.clear();
            pros.store(new FileOutputStream(path, true), ""); // 追加
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Properties支持写入后直接显示中文
    @Test
    public void writePropertiesChinese() {
        Properties pros = new Properties();
        try {
            OutputStream outputStream = new FileOutputStream(path);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            pros.setProperty("username" + Math.random(), "myname");
            pros.setProperty("chinese" + Math.random(), "中文");
            pros.store(bw, null);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历Properties，Properties继承于HashTable.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * 
     */
    @Test
    public void IteratorProperties() throws Exception {
        Properties pros = new Properties();
        pros.load(new FileInputStream(path));
        Iterator it = pros.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + ":" + value);
        }
    }
}
