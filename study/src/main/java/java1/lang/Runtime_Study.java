/*
 * 文件名：Runtime_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Runtime_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月18日
 * 修改内容：新增
 */
package java1.lang;

import java.io.IOException;

import org.junit.Test;

/**
 * @author yunhai
 */

public class Runtime_Study {
    /**
     * 访问JVM相关信息。
     */
    @Test
    public void getJVMInfo() {
        // 获取Java程序关联的运行时对象
        Runtime rt = Runtime.getRuntime();
        System.out.println("处理器数量：" + rt.availableProcessors());
        System.out.println("空闲内存：" + rt.freeMemory()); // measured in bytes
        System.out.println("总内存：" + rt.totalMemory());
        System.out.println("最大内存：" + rt.maxMemory());
    }

    /**
     * 直接单独启动一个进程运行操作系统的命令.
     * 
     */
    @Test
    public void execTest() {
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("notepad.exe"); // 运行记事本
            rt.exec("stikynot"); // 运行便笺
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
