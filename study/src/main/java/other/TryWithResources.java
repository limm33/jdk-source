/*
 * 文件名：TryWithResources.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TryWithResources.java
 * 修改人：zxiaofan
 * 修改时间：2017年4月21日
 * 修改内容：新增
 */
package other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Test;

/**
 * Try-with-resources是java7中一个新的异常处理机制，它能够很容易地关闭在try-catch语句块中使用的资源.
 * 
 * 当程序运行离开try语句块时，资源将按照他们被创建顺序的逆序来关闭。
 * 
 * Note：期望自动关闭的资源必须实现java.lang.AutoCloseable接口。
 * 
 * 可替换繁琐的Try-Catch-Finally风格代码。
 * 
 * @author zxiaofan
 */
public class TryWithResources {
    /**
     * 文本路径.
     */
    String path = "D:\\json.txt";

    /**
     * Try-Catch-Finally旧风格.
     * 
     */
    @Test
    public void testTryCatchFinally() {
        File file = new File(path);
        FileInputStream input = null;
        InputStreamReader in = null;
        try {
            input = new FileInputStream(file);
            in = new InputStreamReader(input);
            String conyent = transformInputStream(in);
            System.out.println(conyent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(input, in);
        }
    }

    /**
     * Try-with-resources风格.
     * 
     */
    @Test
    public void testTryWithResources() {
        File file = new File(path);
        try (FileInputStream input = new FileInputStream(file); InputStreamReader in = new InputStreamReader(input)) {
            String content = transformInputStream(in);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Try-with-resources风格自定义AutoCloseable.
     * 
     */
    @Test
    public void testMyAutoCloseable() {
        try (MyAutoClosable myAutoClosable = new MyAutoClosable()) {
            myAutoClosable.execute();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 批量关闭文件流.
     * 
     * @param closeables
     *            文件流集合
     */
    private void close(AutoCloseable... closeables) {
        if (null != closeables && closeables.length > 0) {
            for (AutoCloseable autoCloseable : closeables) {
                if (null != autoCloseable) {
                    try {
                        autoCloseable.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将文件流转换为字符串.
     * 
     * @param in
     *            in
     * @return 字符串
     * @throws IOException
     *             异常
     */
    private String transformInputStream(InputStreamReader in) throws IOException {
        StringWriter output = new StringWriter();
        char[] buffer = new char[4096];
        for (int n = 0; -1 != (n = in.read(buffer));) {
            output.write(buffer, 0, n);
        }
        String conyent = output.toString();
        return conyent;
    }

}

/**
 * 自定义AutoCloseable.
 * 
 * @author yunhai
 *
 */
class MyAutoClosable implements AutoCloseable {

    /**
     * 定义接口方法.
     * 
     */
    public void execute() {
        System.out.println("execute...");
    }

    @Override
    public void close() throws Exception {
        System.out.println("closed...");
    }
}
