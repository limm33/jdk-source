/*
 * 文件名：addPackage.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： addPackage.java
 * 修改人：yunhai
 * 修改时间：2016年1月11日
 * 修改内容：新增
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

/**
 * AutoPackageAndImport:自动导入package，并且修改错误的import.
 * 
 * @author yunhai
 */
public class AutoPackageAndImport {
    private static String contentPackage = "package ";

    private static String contentImport = "import ";

    private static String lineBreak = "\r\n"; // 换行符

    private static String pathStart = "src";

    private static String encode = "utf-8";

    private static String backupAdd = "@"; // add package时文件备份后缀

    private static String backupImport = "#"; // 修改import时文件备份后缀

    private static boolean deleteBackup = true; // 是否删除备份文件

    private static String noteMsg = "本程序自动增加package；无法自动导包，只能对原有的import进行处理！(默认将删除备份文件)";

    private Map<String, String> maps = new HashMap<String, String>(); // 储存类名及所在的相对路径

    @Test
    public void testAutoPackageAndImport() {
        System.out.println(noteMsg);
        System.out.println("请输入待修改的文件（夹）的绝对路径：");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        String relativeSrc = "";
        try {
            relativeSrc = (path.substring(path.indexOf(pathStart) + 4)); // 相对路径
        } catch (Exception e) {
        }
        // System.out.println(replace(relativeSrc));
        execute(path, true); // AddPackage
        execute(path, false); // changeImport
    }

    /**
     * 执行方法.
     * 
     * @param filePath
     * @param bool
     *            true:遍历所有Java文件并add package；false:修改import的路径
     */
    private void execute(String filePath, boolean bool) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归调用
                execute(file.getAbsolutePath(), bool);
                // System.out.println(filePath + "目录下所有子目录及其文件" + file.getAbsolutePath());
            } else {
                // System.out.println(filePath + "目录下所有文件" + file.getAbsolutePath());
                if (file.getAbsolutePath().endsWith(".java")) {
                    if (bool) {
                        String path = replace(file.getAbsolutePath().replace(".java", ""));
                        try {
                            maps.put(path.substring(path.lastIndexOf(".")), path.substring(path.indexOf(pathStart) + 4));
                            add(file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            changeImport(file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 修改import路径.
     * 
     * @param absolutePath
     * @throws IOException
     */
    private void changeImport(String absolutePath) throws IOException {
        LineNumberReader reader = null;
        BufferedWriter bufWriter = null;
        String s = "";
        List<Object> list = copyAddReturn(absolutePath, false);
        reader = (LineNumberReader) list.get(0);
        bufWriter = (BufferedWriter) list.get(1);
        s = reader.readLine();
        if (s == null) {
            return;
        }
        boolean bool = true;
        String oldChar = "", newChar = "";
        while (s != null) {
            if (bool && (s.startsWith(contentImport) || s == "")) { // 修改原有import
                oldChar = s.substring(s.indexOf(".")).replace(";", "");
                newChar = maps.get(oldChar);
                if (newChar != null && !newChar.equals("")) { // 无需修改
                    s = contentImport + newChar + ";";
                }
                bufWriter.append(s + lineBreak);

            } else {
                bufWriter.append(s + lineBreak);
            }
            if (bool && (s.startsWith("@") || s.startsWith("public"))) {
                bool = false;
            }
            s = reader.readLine();
        }
        reader.close();
        bufWriter.flush();
        bufWriter.close();
        deleteBackup(absolutePath, deleteBackup);
    }

    /**
     * add package.
     * 
     * @throws IOException
     */
    private void add(String absolutePath) throws IOException {
        String insertContent = "";
        try {
            insertContent = contentPackage + replace(absolutePath.substring(absolutePath.indexOf(pathStart) + 4, absolutePath.lastIndexOf("\\"))) + ";" + lineBreak;
        } catch (Exception e) {
            // System.out.println(absolutePath + "在默认目录下,可忽略");
        }
        LineNumberReader reader = null;
        BufferedWriter bufWriter = null;
        List<Object> list = copyAddReturn(absolutePath, true);
        try {
            reader = (LineNumberReader) list.get(0);
            bufWriter = (BufferedWriter) list.get(1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String s = reader.readLine();
        if (s == null) {
            return;
        }
        try {
            while (s.startsWith(contentPackage)) {
                s = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Maybe reader.readLine() is null!");
        }
        boolean bool = true;
        bufWriter.append(insertContent);
        while (s != null) {
            if (bool && s.startsWith(contentImport)) {
                bool = false;
            }
            if (bool && s.startsWith(contentPackage)) {
                s = reader.readLine();
                bool = false;
            }
            bufWriter.append(s + lineBreak);
            s = reader.readLine();
        }
        reader.close();
        bufWriter.flush();
        bufWriter.close();
        deleteBackup(absolutePath, deleteBackup);

    }

    /**
     * 删除备份文件.
     * 
     * @param absolutePath
     */
    private void deleteBackup(String absolutePath, boolean bool) {
        if (!bool) {
            return;
        }
        File fAdd = new File(absolutePath + backupAdd); // 要删除的文件位置
        if (fAdd.exists())
            fAdd.delete();
        File f = new File(absolutePath + backupImport);
        if (f.exists())
            f.delete();
    }

    /**
     * 备份文件并返回LineNumberReader、BufferedWriter.
     * 
     * @param absolutePath
     * @param b
     * @return
     */
    private List<Object> copyAddReturn(String absolutePath, boolean b) {
        String temp = backupImport;
        if (b) {
            temp = backupAdd; // add package操作的备份路径后缀
        }
        String pathCopy = absolutePath + temp;
        List<Object> list = new ArrayList<Object>();
        try {
            FileInputStream fosfrom = new FileInputStream(absolutePath);
            FileOutputStream fosto = new FileOutputStream(pathCopy);
            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            // 关闭输入、输出流
            fosfrom.close();
            fosto.close();

            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(pathCopy), encode));
            BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(absolutePath), encode));
            list.add(reader);
            list.add(bufWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将\替换为点.
     */
    private String replace(String str) {
        return str.replaceAll("\\\\", ".");
    }

}
