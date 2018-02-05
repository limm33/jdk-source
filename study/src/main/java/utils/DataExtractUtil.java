/*
 * 文件名：DataExtractUtil.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： DataExtractUtil.java
 * 修改人：yunhai
 * 修改时间：2016年7月4日
 * 修改内容：新增
 */
package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.google.gson.Gson;

/**
 * 需引入Jsoup和Gson包
 * 
 * 持续更新地址：https://github.com/zxiaofan/JDK-Study/tree/master/src/utils
 * 
 * @author yunhai
 */
public class DataExtractUtil {
    private static String databaseName = "ComptPriceOrderOperLog"; // 基础数据库名

    private static String tableNameBasic = "OrderOperLog"; // 基础表名

    private static String timeStart = "20160601"; // 按年分表只需填写年份,按天分表需年月日格式

    private static String timeEnd = "20160630";

    private static int timeout = 9000; // 超时设置

    // sql中的所有表名请用dynamicTableName代替
    private static String sql = "sql";

    public static void main(String[] args) throws Exception {
        DataExtractUtil.start();
    }

    public static void start() throws Exception {
        System.out.println("sql中的所有表名请用dynamicTableName代替");
        List<String> tableList = getAllTableName(timeStart, timeEnd);
        createFile(pathSave);
        StringBuffer allData = new StringBuffer();
        for (String tableName : tableList) {
            querySave(tableName, allData);
        }
        fileSave(allData.toString(), pathSave + tableNameBasic + timeStart + "-" + timeEnd + ".txt");
        openFolder(pathSave);
    }

    public static void openFolder(String pathFolder) {
        System.out.println("操作完成,请到" + pathFolder + "查看结果！");
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = pathFolder;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成所有表名.
     * 
     * @param timeStart2
     * @param timeEnd2
     * @return
     * @throws Exception
     */
    private static List<String> getAllTableName(String timeStart2, String timeEnd2) throws Exception {
        if (timeStart2.length() != timeEnd2.length() || timeStart2.length() < 4 || timeStart2.length() > 8) {
            throw new RuntimeException("时间格式错误");
        }
        SimpleDateFormat format = new SimpleDateFormat();
        int calendarAdd = 0;
        if (timeStart2.length() == 4) {
            format = formaty;
            calendarAdd = Calendar.YEAR;
        } else if (timeStart2.length() == 6) {
            format = formatM;
            calendarAdd = Calendar.MONTH;
        } else if (timeStart2.length() == 8) {
            format = formatd;
            calendarAdd = Calendar.DATE;
        }
        List<String> tableList = new ArrayList<>();
        Date date1 = format.parse(timeStart2);
        Date date2 = format.parse(timeEnd2);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        while (!calendar1.after(calendar2)) {
            tableList.add(tableNameBasic + format.format(calendar1.getTime()));
            calendar1.add(calendarAdd, 1);
        }
        return tableList;
    }

    public static void querySave(String tableName, StringBuffer allData) throws Exception {
        Map<String, String> queryMap = new HashMap<String, String>();
        String querySql = sql;
        queryVo vo = new queryVo();
        vo.setDatabaseName(databaseName);
        querySql = querySql.replaceAll("dynamicTableName", tableName);
        // querySql.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
        querySql.replaceAll("\\u0027", "'");
        vo.setSql(querySql);
        queryMap.put("data", gson.toJson(vo));
        Document doc = jsoupDoc(url, queryMap, Method.POST);
        Map<String, String> map = gson.fromJson(doc.text(), Map.class);
        Document docRes = jsoupDoc(referer + map.get("msg"), queryMap, Method.GET);
        String result = keepLineBreak(docRes);
        allData.append(result);
        fileSave(result, pathSave + tableName + ".txt");
    }

    /**
     * 保留换行符.
     */
    public static String keepLineBreak(Document docRes) {
        docRes.outputSettings(new Document.OutputSettings().prettyPrint(false));// makes html() preserve linebreaks and spacing
        docRes.select("br").append("\\n");
        docRes.select("p").prepend("\\n\\n");
        String result = docRes.html().replaceAll("\\\\n", "\n");
        result = Jsoup.clean(result, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        return result;
    }

    public static Document jsoupDoc(String url, Map<String, String> map, Method method) throws Exception {
        Connection connection = Jsoup.connect(url);
        connection.timeout(timeout); // 设置连接超时时间
        // 给服务器发消息头，告诉服务器，俺不是java程序。
        connection.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
        connection.header("Accept", "application/json, text/javascript, */*; q=0.01");
        connection.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.header("Host", "172.16.7.21:4385");
        connection.header("Accept-Encoding", "gzip, deflate");
        Response doc = null;
        try {
            if (Method.POST.equals(method)) {
                connection.post();
            } else {
                connection.get();
            }
            doc = connection.ignoreContentType(true).method(method).data(map).execute();
        } catch (IOException e) {
            System.err.println(url);
            e.printStackTrace();
        } // 获取返回的html的document对象
        return doc.parse();
    }

    private static void fileSave(String param, String path) {
        // 输出到本地文本
        createFile(path);
        File file = new File(path);
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            out.append(param);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建文件（夹）.
     * 
     * @param path
     *            路径
     */
    private static void createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (path.contains(".")) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 递归创建文件夹，保证该路径所有文件夹都被创建
                createFile(path.substring(0, path.lastIndexOf("\\")));
                file.mkdir();
            }
        }
    }

    private static String pathSave = "d:\\dataSave\\"; // 保存位置

    private static String url = "http://172.16.7.21:4385/Database/queryData";

    private static String referer = "http://172.16.7.21:4385/";

    private static SimpleDateFormat formaty = new SimpleDateFormat("yyyy");

    private static SimpleDateFormat formatM = new SimpleDateFormat("yyyyMM");

    private static SimpleDateFormat formatd = new SimpleDateFormat("yyyyMMdd");

    // private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); // 保留特殊字符
    private static Gson gson = new Gson();

}

class queryVo {
    private String databaseName;

    private String databaseType = "1";

    private String sql;

    private String ruleValue = "";

    private String ruleType = "0";

    private String dataPlace = "1";

    private String isRead = "1";

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getDataPlace() {
        return dataPlace;
    }

    public void setDataPlace(String dataPlace) {
        this.dataPlace = dataPlace;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

}