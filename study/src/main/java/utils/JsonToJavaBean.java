/*
 * 文件名：JsonToJavaBean.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： JsonToJavaBean.java
 * 修改人：zxiaofan
 * 修改时间：2016年7月20日
 * 修改内容：新增
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * 最近需将大量json转换为对应的JavaBean，网上有诸多在线转换工具，但是由于[1:无法直接生成JavaBean;2转换不够准确;3:公司对代码要求CheckStyle]，故自己写个转换工具
 * 
 * 功能：读取Json文件并在outputPath目录生成相应的JavaBean文件，直接Copy使用，注释可选(直过CheckStyle)。
 * 
 * 持续更新地址：https://github.com/zxiaofan/JDK-Study/tree/master/src/utils
 * 
 * @author zxiaofan
 */
@SuppressWarnings("unchecked")
public class JsonToJavaBean {
    // ====== 修改参数 ====== //
    private String path = "D:\\json.txt"; // 待转换Json路径

    private static String packageName = "com.github.zxiaofan"; // packageName包名.

    private String beanRootName = "Root"; // 根Bean名字
    // ====== 修改参数 ====== //

    private boolean addNote = true; // 是否添加注释

    private boolean defaultInteger = true; // 默认使用Integer代替int

    private static String packagePath = "model.vo"; // package默认加入该路径

    private static String outputPath = "d:\\JsonToJavaBean\\"; // 输出路径

    // [double]特殊匹配规则(前缀|后缀|包含|类型),默认不区分大小写
    private static String matchRuleDouble = "|Date_Time_Hour||long,|!id_type|money_price|BigDecimal,|Cost|!time|BigDecimal";

    // [String]特殊匹配规则(前缀|后缀|包含|类型),默认不区分大小写
    private static String matchRuleString = "|Date_Time_Hour||Date," + matchRuleDouble; // 自动覆盖double规则第一条

    private static boolean ismachRule = true; // 是否开启特殊匹配规则

    private static boolean machRuleIgnoreCase = true; // 特殊匹配规则默认不区分大小写

    static JsonToJavaBean start = new JsonToJavaBean();

    public static void main(String[] args) {
        start.start();
    }

    public void start() {
        // System.out.println("请输入待转换的json文件的绝对路径：");
        // Scanner scanner = new Scanner(System.in);
        // String path = scanner.nextLine();

        String json = readTextFile(path, encode);
        outputPath = outputPath + format.format(new Date()) + "\\" + packageName + "\\" + packagePath;
        outputPath = outputPath.replaceAll("\\.", "\\\\");
        toJavaBean(json, packageName);
        System.out.println("转换完成，请到【" + outputPath + "】查看转换结果！");
        createFile(outputPath);
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = outputPath;
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 转bean.
     * 
     * @param json
     * @param packageName
     */
    private void toJavaBean(String json, String packageName) {
        if (!(json.startsWith("{") || json.endsWith("}"))) {
            throw new RuntimeException("不是标准的json文件:{...}"); // 暂不做过多校验
        }
        json = formatStr(json);
        buildRule(matchRuleDouble, listRuleDouble);
        buildRule(matchRuleString, listRuleString);
        List<Bean> beans = new ArrayList<>();
        buildOrignBean(json, beans, beanRootName);
        formatBean();
        createJavaModel();
        // System.out.println(gson.toJson(fields));
    }

    /**
     * 生成Java model.
     * 
     */
    private void createJavaModel() {
        createFile(outputPath);
        for (Entry<String, List<Bean>> entry : fields.entrySet()) {
            String[] listJar = new String[8]; // 顺序:BigDecimal、Date、List、Map、Set
            List<Bean> fieldVos = entry.getValue();
            if (fieldVos.isEmpty()) {
                continue;
            }
            StringBuffer buffer = new StringBuffer();
            if (!"".equals(packageName) && packageName != null && !packageName.endsWith(".")) {
                packageName += ".";
            }
            buffer.append("package " + packageName + packagePath + ";" + rn + rn);
            buffer.append(importJar);
            buffer.append(author);
            buffer.append("public class " + fieldVos.get(0).getFieldNameUpper() + " {" + rn);
            // 字段定义
            for (int i = 1; i < fieldVos.size(); i++) {
                Bean vo = fieldVos.get(i);
                if (addNote) {
                    buffer.append(desc1);
                    buffer.append(vo.getFieldDesc());
                    buffer.append(desc2);
                }
                buffer.append("private " + vo.getFieldType() + " " + vo.getFieldNameLower() + ";" + rn);
                if (vo.getFieldType().startsWith(ObjType.BigDecimal)) {
                    listJar[0] = importBigDecimal;
                } else if (vo.getFieldType().startsWith(ObjType.Date)) {
                    listJar[1] = importDate;
                } else if (vo.getFieldType().startsWith(ObjType.List)) {
                    listJar[2] = importList;
                } else if (vo.getFieldType().startsWith(ObjType.Map)) {
                    listJar[3] = importMap;
                } else if (vo.getFieldType().startsWith(ObjType.Set)) {
                    listJar[4] = importSet;
                }
            }
            // 字段get、set
            for (int i = 1; i < fieldVos.size(); i++) {
                Bean vo = fieldVos.get(i);
                // get
                if (addNote) {
                    buffer.append(desc1);
                    buffer.append("获取" + vo.getFieldDesc() + "." + rn + tab + " *" + rn);
                    buffer.append("     * @return 返回" + vo.getFieldDesc());
                    buffer.append(desc2);
                }
                buffer.append("public " + vo.getFieldType() + " get" + vo.getFieldNameUpper() + "() {" + rn);
                buffer.append(tab + tab + "return " + vo.getFieldNameLower() + ";" + rn + tab + "}" + rn);
                // set
                if (addNote) {
                    buffer.append(desc1);
                    buffer.append("设置" + vo.getFieldDesc() + "." + rn + tab + " *" + rn);
                    buffer.append("     * @param " + vo.getFieldNameLower() + rn + tab);
                    buffer.append(" *       要设置的" + vo.getFieldNameUpper());
                    buffer.append(desc2);
                }
                buffer.append("public void set" + vo.getFieldNameUpper() + "(" + vo.getFieldType() + " " + vo.getFieldNameLower() + ") {" + rn);
                buffer.append(tab + tab + "this." + vo.getFieldNameLower() + " = " + vo.getFieldNameLower() + ";" + rn + tab + "}" + rn);
            }
            buffer.append("}" + rn);
            String beanText = buffer.toString();
            StringBuffer jarText = new StringBuffer();
            for (String jar : listJar) {
                if (jar != null) {
                    jarText.append(jar);
                }
            }
            beanText = beanText.replace(importJar, jarText.toString());
            String finalPath = outputPath + "\\";
            finalPath = finalPath.replaceAll("\\.", "\\\\");
            createFile(finalPath);
            finalPath = finalPath + fieldVos.get(0).getFieldNameUpper() + ".java";
            try {
                FileWriter fw = new FileWriter(finalPath);
                fw.write(beanText);
                fw.close();
                // OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), encode);
                // out.write(buffer.toString());
                // out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化Bean中字段值.
     * 
     * @param fieldVos
     *            fieldVos
     */
    private void formatBean() {
        for (List<Bean> fieldVos : fields.values()) {
            for (Bean bean : fieldVos) {
                bean.setFieldNameLower(CaseConversion(bean.getFieldName(), true));
                bean.setFieldNameUpper(CaseConversion(bean.getFieldName(), false));
            }
        }
    }

    private void buildOrignBean(String json, List<Bean> beans, String className) {
        Map<String, Object> map = gson.fromJson(json, Map.class);
        Bean beanParent = new Bean();
        beanParent.setFieldName(className);
        beans.add(beanParent);
        Iterator<Entry<String, Object>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Bean bean = new Bean();
            Entry<String, Object> entry = itr.next();
            String k = entry.getKey(); // FieldName
            bean.setFieldName(k);
            Object v = entry.getValue();
            if (v == null || v.toString().equals("[]")) {
                itr.remove();
                continue;
            }
            if (v instanceof Integer) {
                bean.setFieldType(ObjType.Int);
                if (defaultInteger) {
                    bean.setFieldType(ObjType.Integer);
                }
            } else if (v instanceof Double) {
                // bean.setFieldType(ObjType.Double);
                bean.setFieldType(matchRule(k, ObjType.Double, listRuleDouble));
            } else if (v instanceof Boolean) {
                bean.setFieldType(ObjType.Boolean);
            } else if (v instanceof String) {
                bean.setFieldType(ObjType.String);
                bean.setFieldType(matchRule(k, ObjType.String, listRuleString));
            } else {
                String childJson = v.toString();
                if (childJson.startsWith("{")) {
                    bean.setFieldType(CaseConversion(k, false));
                    List<Bean> newBeans = new ArrayList<>();
                    buildOrignBean(childJson, newBeans, k);
                    fields.put(k, newBeans);
                } else if (childJson.startsWith("[")) {
                    bean.setFieldName(k);
                    bean.setFieldType("List<" + CaseConversion(k, false) + ">");
                    // System.out.println(childJson);
                    List<Object> childList = gson.fromJson(childJson, List.class);
                    List<Bean> newBeans = new ArrayList<>();
                    if (!childList.toString().startsWith("[{")) { // 匹配特殊格式["ANY"],Expected BEGIN_OBJECT but was STRING
                        bean.setFieldType(ObjType.ListString);
                        beans.add(bean);
                    } else {
                        buildOrignBean(gson.toJson(childList.get(0)), newBeans, k);
                    }
                } else {
                    bean.setFieldType(ObjType.String);
                }
            }
            beans.add(bean);
        }
        fields.put(className, beans);

    }

    /**
     * 构建特殊匹配规则.
     * 
     */
    private String matchRule(String fieldName, String fieldType, List<String[]> listRule) {
        if (!ismachRule) {
            return fieldType;
        }
        // |Date_Time||long,|!id_type|money_price|BigDecimal,|Cost|!time|BigDecimal
        if (machRuleIgnoreCase) {
            fieldName = fieldName.toLowerCase();
        }
        for (String[] oneRule : listRule) {
            boolean special = true; // 满足特殊匹配规则
            boolean oneRuleDotFail = false; // 不匹配规则中任意一项
            for (int i = 0; i < oneRule.length - 1; i++) {
                if (oneRuleDotFail) {
                    break;
                }
                String[] tempArr = null;
                String tempStr = oneRule[i];
                if (tempStr != null && !"".equals(tempStr)) {
                    boolean fei = false;
                    if (tempStr.startsWith("!")) {
                        fei = true;
                        tempStr = tempStr.replace("!", "");
                    }
                    tempArr = tempStr.split("_"); // 前/后缀规则
                    boolean bool1 = false; // 前缀中若含有两个匹配，满足一个则即为true
                    if (tempArr != null) {
                        for (String str : tempArr) {
                            if (bool1) {
                                break;
                            }
                            if (fei && oneRuleDotFail) {
                                break;
                            }
                            if (machRuleIgnoreCase) {
                                str = str.toLowerCase();
                            }
                            switch (i) {
                                case 0: // 前缀
                                    if (fei ^ fieldName.startsWith(str)) {
                                        bool1 = true;
                                        special = true;
                                    } else {
                                        special = false;
                                        oneRuleDotFail = true; // 不匹配规则中任意一项，直接跳过
                                    }
                                    break;
                                case 1: // 后缀
                                    if (fei ^ fieldName.endsWith(str)) {
                                        bool1 = true;
                                        special = true;
                                    } else {
                                        special = false;
                                        oneRuleDotFail = true;
                                    }
                                    break;
                                case 2: // 包含
                                    if (fei ^ fieldName.contains(str)) {
                                        bool1 = true;
                                        special = true;
                                    } else {
                                        special = false;
                                        oneRuleDotFail = true;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

            }
            if (special) {
                return oneRule[3];
            }
        }
        return fieldType;

    }

    public void buildRule(String matchRule, List<String[]> listRule) {
        if (matchRule.length() != 0 && listRule.isEmpty()) {
            String[] rules = matchRule.split(",");
            for (String rule : rules) {
                listRule.add(rule.split("\\|")); // 自定义规则列表
            }
        }
    }

    private String readTextFile(String sFileName, String sEncode) {
        StringBuffer sbStr = new StringBuffer();
        try {
            File ff = new File(sFileName);
            InputStreamReader read = new InputStreamReader(new FileInputStream(ff), sEncode);
            BufferedReader ins = new BufferedReader(read);
            String dataLine = "";
            while (null != (dataLine = ins.readLine())) {
                sbStr.append(dataLine);
                // sbStr.append("/r/n");
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbStr.toString();
    }

    /**
     * 新建文件（夹）.
     * 
     * @param path
     *            路径
     */
    private void createFile(String path) {
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

    /**
     * 去空格去换行.
     *
     * @param str
     *            str
     * @return str
     */
    private String formatStr(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
            // json = json.replace(":\".*?\"(?=[,}])", ":\"" + typeString + "\"");
            p = Pattern.compile(":\".*?\"(?=[,}])");
            m = p.matcher(str);
            str = m.replaceAll(":\"" + typeString + "\"");

        }
        return str;
    }

    /**
     * 大小写转换.
     * 
     * @param fieldName
     * @param toLower
     *            true:大转小
     * @return
     */
    private String CaseConversion(String fieldName, boolean toLower) {
        if ("".equals(fieldName)) {
            return "";
        }
        String result = "";
        if (toLower) {
            result = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toLowerCase());
        } else {
            result = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        }
        return result;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");

    private static String typeString = "typeString";

    private static String encode = "utf-8";

    private static Gson gson = new Gson();

    private static Map<String, List<Bean>> fields = new HashMap<>();

    private static List<String[]> listRuleDouble = new ArrayList<>(); // 匹配规则

    private static List<String[]> listRuleString = new ArrayList<>(); // 匹配规则

    private static String importJar = "github.zxiaofan.com";

    private static String rn = "\r\n"; // 换行.

    private static String tab = "    ";

    private static String desc1 = tab + "/**" + rn + tab + " * "; // 字段描述-开始部分

    private static String desc2 = "." + rn + tab + " */" + rn + tab;

    private static String author = rn + "/**" + rn + " * @author yunhai(default)" + rn + " */" + rn;

    private static String importList = "import java.util.List;\r\n";

    private static String importBigDecimal = "import java.math.BigDecimal;\r\n";

    private static String importMap = "import java.util.Map;\r\n";

    private static String importDate = "import java.util.Date;\r\n";

    private static String importSet = "import java.util.Set;\r\n";
}

class Bean {
    private String fieldName;

    private String fieldNameLower; // 小写开头

    private String fieldNameUpper; // 大写开头

    private String fieldType; // 类型

    private String fieldDesc; // 描述

    /**
     * 设置fieldName.
     * 
     * @return 返回fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 获取fieldName.
     * 
     * @param fieldName
     *            要设置的fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 设置fieldNameLower.
     * 
     * @return 返回fieldNameLower
     */
    public String getFieldNameLower() {
        return fieldNameLower;
    }

    /**
     * 获取fieldNameLower.
     * 
     * @param fieldNameLower
     *            要设置的fieldNameLower
     */
    public void setFieldNameLower(String fieldNameLower) {
        this.fieldNameLower = fieldNameLower;
    }

    /**
     * 设置fieldNameUpper.
     * 
     * @return 返回fieldNameUpper
     */
    public String getFieldNameUpper() {
        return fieldNameUpper;
    }

    /**
     * 获取fieldNameUpper.
     * 
     * @param fieldNameUpper
     *            要设置的fieldNameUpper
     */
    public void setFieldNameUpper(String fieldNameUpper) {
        this.fieldNameUpper = fieldNameUpper;
    }

    /**
     * 设置fieldType.
     * 
     * @return 返回fieldType
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * 获取fieldType.
     * 
     * @param fieldType
     *            要设置的fieldType
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 设置fieldDesc.
     * 
     * @return 返回fieldDesc
     */
    public String getFieldDesc() {
        return fieldDesc;
    }

    /**
     * 获取fieldDesc.
     * 
     * @param fieldDesc
     *            要设置的fieldDesc
     */
    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

}

class ObjType {
    static final String String = "String";

    static final String Int = "int";

    static final String Integer = "Integer";

    static final String Boolean = "boolean";

    static final String Float = "float";

    static final String Date = "Date";

    static final String Long = "long";

    static final String Double = "double";

    static final String BigDecimal = "BigDecimal";

    static final String List = "List";

    static final String ListString = "List<String>"; // 特殊list专用

    static final String Map = "Map";

    static final String Set = "Set";

    static final String Defined = "Defined";
}