package java1.lang.annotation.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import java1.lang.annotation.SpecialParameterizedType;

/**
 * @author github.zxiaofan.com
 *
 *         参数校验or参数处理
 */
@SuppressWarnings({"rawtypes"})
public final class ValidationUtil {

    /**
     * 构造函数.
     * 
     */
    private ValidationUtil() {
        throw new RuntimeException("this is a util class,can not instance!");
    }

    static ThreadLocal<Gson> localGson = new ThreadLocal<Gson>() {
        protected Gson initialValue() {
            return new Gson();
        };
    };

    static ThreadLocal<Long> localTime = new ThreadLocal<Long>() {
        protected Long initialValue() {
            return System.currentTimeMillis();
        };
    };

    static ThreadLocal<List<String>> localList = new ThreadLocal<List<String>>() {
        protected List<String> initialValue() {
            return new ArrayList<>();
        };
    };

    static ThreadLocal<Boolean> localValidate = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return false;
        };
    };

    // static long getLocalTime() {
    // Long time = localTime.get();
    // if (null == time) {
    // localTime.set(System.currentTimeMillis());
    // }
    // return time;
    // }

    /**
     * 参数校验.
     * 
     * @param obj
     *            obj
     * @return String
     * @throws Exception
     *             Exception
     */
    public static String validate(Object obj) throws Exception {
        localValidate.set(true);
        localTime.set(System.currentTimeMillis());
        localList.set(new ArrayList<>());
        splitObject(obj);
        String result = localList.get().isEmpty() ? null : packaging(localList.get());
        clearLocal();
        return result;

    }

    /**
     * 清理local实例.
     * 
     */
    private static void clearLocal() {
        localValidate.remove();
        localList.remove();
        localTime.remove();
        localGson.remove();
    }

    /**
     * 参数处理.
     * 
     * @param obj
     *            obj
     * @return obj
     * @throws Exception
     *             e
     */
    public static Object dealParam(Object obj) throws Exception {
        Object result = splitObject(obj);
        clearLocal();
        return result;
    }

    /**
     * 分而治之，处理集合.
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static Object splitObject(Object obj) throws Exception {
        if (null == obj) {
            return null;
        }
        // 处理集合
        if (obj instanceof List) {
            List<Class> listRealClass = getRealClass(obj);
            List<Object> listNew = (List<Object>) obj.getClass().newInstance();
            List<Object> listOld = (List) obj; // gson.fromJson(gson.toJson(obj), List.class);
            for (int i = 0; i < listOld.size(); i++) {
                Object objNew = listOld.get(i);
                objNew = localGson.get().fromJson(localGson.get().toJson(objNew), listRealClass.get(i));
                objNew = splitObject(objNew);
                listNew.add(objNew);
            }
            obj = listNew;
        } else if (obj instanceof Set) {
            List<Class> listRealClass = getRealClass(obj);
            Set<Object> setNew = (Set<Object>) obj.getClass().newInstance();
            Set<Object> setOld = (Set) obj; // gson.fromJson(gson.toJson(obj), Set.class);
            for (Object set : setOld) {
                int i = 0;
                Object objNew = localGson.get().fromJson(localGson.get().toJson(set), listRealClass.get(i));
                objNew = splitObject(objNew);
                setNew.add(objNew);
                i++;
            }
            obj = setNew;
        } else if (obj instanceof Map) {
            Map<?, ?> mapClass = getRealClassOfMap((Map<?, ?>) obj);
            Map<Object, Object> mapNew = (Map<Object, Object>) obj.getClass().newInstance();
            Map<?, ?> mapOld = (Map<?, ?>) obj; // gson.fromJson(gson.toJson(obj), Map.class);
            for (Entry<?, ?> set : mapOld.entrySet()) {
                Iterator ite = mapClass.entrySet().iterator();
                @SuppressWarnings("unused")
                Entry entry = null;
                if (ite.hasNext()) {
                    entry = (Entry) ite.next();
                    Object objKey = splitObject(set.getKey());
                    // objKey = gson.fromJson(set.getKey().toString(), (Class) entry.getKey());
                    objKey = splitObject(objKey);
                    Object objValue = splitObject(set.getValue());
                    // objValue = gson.fromJson(gson.toJson(set.getValue()), (Class) entry.getValue());
                    mapNew.put(objKey, objValue);
                }
            }
            obj = mapNew;
        } else { // Object 或 泛型T
            dealBasicObject(obj);
        }
        return obj;
    }

    /**
     * 处理泛型Object.
     * 
     * @param obj
     *            obj
     * @param field
     *            field
     * @throws Exception
     *             Exception
     */
    private static void dealGenericObj(Object obj, Field field) throws Exception {
        // List<Class> listGenericClass = getGenericClass(obj, genericName);
        field.setAccessible(true);
        Object objGen = null;
        objGen = splitObject(field.get(obj)); // 避免Request<List<AnnoVo>>
        field.set(obj, objGen);
    }

    /**
     * Type -> Class(暂未使用)
     */
    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    /**
     * 该属性是泛型.
     * 
     * @param field
     *            field
     * @return isGeneric
     */
    private static boolean isGeneric(Field field) {
        if (field.getGenericType() instanceof ParameterizedType || field.getGenericType() instanceof TypeVariable || field.getGenericType() instanceof GenericArrayType) {
            return true;
        }
        return false;
    }

    /**
     * 判断Object是否有特定注解（没有则不遍历内部属性）.
     * 
     * @param obj
     *            obj
     * @return hasAnno
     */
    private static boolean hasAnnoOf(Object obj, Annotation an) {
        Annotation[] classAnnotation = obj.getClass().getAnnotations();
        boolean isTooLong = false;
        for (Annotation annotation : classAnnotation) {
            Class annotationType = annotation.annotationType();
            if (annotationType.getName().equals(an.annotationType().getName())) {
                isTooLong = true;
                break;
            }
        }
        return isTooLong;
    }

    /**
     * 处理非集合类Object，包括泛型.
     * 
     * @param obj
     *            obj
     */
    private static void dealBasicObject(Object obj) {
        String genericName = null;
        String geStr = getGenericType(obj);
        if (geStr.endsWith(">")) {
            genericName = geStr.substring(geStr.lastIndexOf("<") + 1, geStr.length() - 1);
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Type type = field.getGenericType();
                boolean isGeneric = isGeneric(field);
                if (isGeneric && null != genericName && genericName.equals(type.toString())) {
                    dealGenericObj(obj, field);
                } else {
                    dealSimpleObject(obj, field);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Java 7 获取Request<T>的T;Java8直接使用obj.getClass().toGenericString()即可.
     * 
     * @param obj
     *            obj
     * @return T
     */
    private static String getGenericType(Object obj) {
        // if Java8 //return obj.getClass().toGenericString()
        StringBuffer sb = new StringBuffer();
        TypeVariable<?>[] typeparms = obj.getClass().getTypeParameters();
        if (typeparms.length > 0) {
            boolean first = true;
            sb.append('<');
            for (TypeVariable<?> typeparm : typeparms) {
                if (!first)
                    sb.append(',');
                sb.append(typeparm.getName());
                first = false;
            }
            sb.append('>');
        }
        return sb.toString();
    }

    private static List<Class> getRealClass(Object obj) throws IllegalAccessException {
        List<Class> listRealClass = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        if (obj instanceof List) {
            for (Field fd : fields) {
                fd.setAccessible(true);
                if ("elementData".equals(fd.getName())) {
                    Object[] objs = (Object[]) fd.get(obj);
                    for (Object object : objs) {
                        if (null != object) {
                            listRealClass.add(object.getClass());
                        }
                    }
                }
            }
        } else if (obj instanceof Set) {
            for (Field fd : fields) {
                if ("map".equals(fd.getName())) {
                    fd.setAccessible(true);
                    Map<?, ?> map = (Map<?, ?>) fd.get(obj);
                    for (Object object : map.keySet()) {
                        if (null != object) {
                            listRealClass.add(object.getClass());
                        }
                    }
                }
            }
        } else {
            listRealClass.add(obj.getClass());
        }
        return listRealClass;
    }

    private static Map<Object, Object> getRealClassOfMap(Map<?, ?> obj) throws IllegalAccessException {
        Map<Object, Object> mapClass = new HashMap<Object, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field fd : fields) {
            fd.setAccessible(true);
            if ("table".equals(fd.getName())) {
                Object key = null;
                Object value = null;
                for (Entry<?, ?> entry : obj.entrySet()) {
                    if (null != entry) {
                        Class keyClass = getRealClass(entry.getKey()).get(0);
                        Type typeKey = null;
                        if (entry.getKey() instanceof List) {
                            typeKey = new SpecialParameterizedType(List.class, keyClass);
                        } else if (entry.getKey() instanceof Set) {
                            typeKey = new SpecialParameterizedType(Set.class, keyClass);
                        } else if (entry.getKey() instanceof Map) {
                            typeKey = new SpecialParameterizedType(Map.class, keyClass);
                        }
                        Class valueClass = getRealClass(entry.getValue()).get(0);
                        Type typeValue = null;
                        if (entry.getValue() instanceof List) {
                            typeValue = new SpecialParameterizedType(List.class, keyClass);
                        } else if (entry.getValue() instanceof Set) {
                            typeValue = new SpecialParameterizedType(Set.class, keyClass);
                        } else if (entry.getValue() instanceof Map) {
                            typeValue = new SpecialParameterizedType(Map.class, keyClass);
                        }
                        if (null != typeKey) {
                            key = typeKey;
                        } else {
                            key = keyClass;
                        }
                        if (null != typeValue) {
                            value = typeValue;
                        } else {
                            value = valueClass;
                        }
                        mapClass.put(key, value);
                    }
                }
            }
        }
        return mapClass;
    }

    /**
     * 处理基础Object.
     * 
     * @param obj
     *            参数
     * @param claName
     *            类名
     * @param field
     *            属性
     * @throws Exception
     *             e
     */
    private static void dealSimpleObject(Object obj, Field field) throws Exception {
        // boolean isTooLong = hasAnnoOfStringCut(obj);
        // if (!isTooLong) { // 没有特定注解则不处理此Bean
        // return;
        // }
        Class type = field.getType();
        field.setAccessible(true);
        Object value = field.get(obj);
        if (isPrimitive_Package_String(type)) {
            if (localValidate.get()) {
                realValidate(obj, field);
            } else {
                realDeal(obj, field);
            }
        } else { // 内部还包含Object类型
            value = splitObject(value);
            field.set(obj, value);
        }
    }

    /**
     * 数据处理最终逻辑.
     * 
     * @param obj
     * @param field
     * @throws Exception
     */
    private static void realDeal(Object obj, Field field) throws Exception {
        Annotation[] annotations = field.getAnnotations();
        ParamVo paramVo = initDealParam(obj, field);
        for (Annotation an : annotations) {
            if (an.annotationType().getName().equals(StringCut.class.getName())) {
                dealStringCut(paramVo, an);
            } else if (an.annotationType().getName().equals(ToLower.class.getName())) {
                dealToLower(paramVo, an);
            } else if (an.annotationType().getName().equals(ToUpper.class.getName())) {
                dealToUpper(paramVo, an);
            } else if (an.annotationType().getName().equals(Number.class.getName())) {
                dealNumber(paramVo, an);
            }
        }
    }

    /**
     * 是否是基本类型或其包装类或String/Date/Timestamp/BigDecimal.
     * 
     * @param cla
     *            Class
     * @return boolean
     */
    private static boolean isPrimitive_Package_String(Class cla) {
        if (null == cla) {
            return false;
        }
        String claName = cla.getName();
        if (cla.isPrimitive() || String.class.getName().equals(claName)) {
            return true;
        }
        if (Integer.class.getName().equals(claName) || Double.class.getName().equals(claName) || Float.class.getName().equals(claName) || Long.class.getName().equals(claName)
                || Short.class.getName().equals(claName) || Byte.class.getName().equals(claName) || Boolean.class.getName().equals(claName) || Character.class.getName().equals(claName)) {
            return true;
        }
        if (Date.class.getName().equals(claName) || Timestamp.class.getName().equals(claName) || BigDecimal.class.getName().equals(claName)) {
            return true;
        }
        return false;
    }

    /**
     * 处理含有@StringCut的属性，超长则截取.
     * 
     * @param obj
     *            对象
     * @param field
     *            属性
     * @param an
     *            注解
     * @throws Exception
     *
     */
    private static void dealStringCut(ParamVo paramVo, Annotation an) throws Exception {
        StringCut stringCut = (StringCut) an;
        Field field = paramVo.getField();
        Object obj = paramVo.getObj();
        Class type = field.getType();
        Object value = field.get(obj);
        if (String.class.equals(type)) {
            if (null != value && value.toString().length() > stringCut.maxLength() && stringCut.maxLength() != 0) {
                try {
                    // WriteLogBusi.writeLocalLog(fieldName, value.toString());
                    if (stringCut.isKeepHead()) {
                        field.set(obj, value.toString().substring(0, stringCut.maxLength()));
                    } else {
                        field.set(obj, value.toString().substring(value.toString().length() - stringCut.maxLength()));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // WriteLogBusi.writeLocalLog(fieldName, e.getMessage() + e.toString());
                }
            }
            if (null != value && value.toString().length() < stringCut.minLength()) {
                int addLength = stringCut.minLength() - value.toString().length();
                String addValue = "";
                while (addValue.length() < addLength) {
                    addValue += stringCut.completion();
                }
                addValue = addValue.substring(0, addLength);
                try {
                    // WriteLogBusi.writeLocalLog(fieldName, value.toString());
                    if (stringCut.isKeepHead()) {
                        field.set(obj, value.toString() + addValue);
                    } else {
                        field.set(obj, addValue + value.toString());
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // WriteLogBusi.writeLocalLog(fieldName, e.getMessage() + e.toString());
                }
            }
        }
    }

    /**
     * 包装参数错误信息.
     * 
     * @param list
     *            结果
     * @return 结果
     */
    private static String packaging(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s).append(';');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 参数校验最终逻辑.
     * 
     * @param obj
     *            参数
     * @param field
     *            属性
     * @throws Exception
     *             e
     */
    private static void realValidate(Object obj, Field field) throws Exception {
        Annotation[] annotations = field.getAnnotations();
        field.setAccessible(true);
        ParamVo paramVo = initDealParam(obj, field);
        for (Annotation an : annotations) {
            if (an.annotationType().getName().equals(Null.class.getName())) {
                validateNull(paramVo, an);
            } else if (an.annotationType().getName().equals(NotNull.class.getName())) {
                validateNotNull(paramVo, an);
            } else if (an.annotationType().getName().equals(AssertFalse.class.getName())) {
                validateAssertFalse(paramVo, an);
            } else if (an.annotationType().getName().equals(AssertTrue.class.getName())) {
                validateAssertTrue(paramVo, an);
            } else if (an.annotationType().getName().equals(Email.class.getName())) {
                validateEmail(paramVo, an);
            } else if (an.annotationType().getName().equals(FutureTime.class.getName())) {
                validateFutureTime(paramVo, an);
            } else if (an.annotationType().getName().equals(StringLength.class.getName())) {
                validateStringLength(paramVo, an);
            } else if (an.annotationType().getName().equals(PastTime.class.getName())) {
                validatePastTime(paramVo, an);
            } else if (an.annotationType().getName().equals(Pattern.class.getName())) {
                validatePattern(paramVo, an);
            } else if (an.annotationType().getName().equals(Phone.class.getName())) {
                validatePhone(paramVo, an);
            } else if (an.annotationType().getName().equals(Postcode.class.getName())) {
                validatePostCode(paramVo, an);
            } else if (an.annotationType().getName().equals(Tel.class.getName())) {
                validateTel(paramVo, an);
            } else if (an.annotationType().getName().equals(NotNullAndEmpty.class.getName())) {
                validateNotNullAndEmpty(paramVo, an);
            }
            paramVo.setScope(null);
            paramVo.setParamLimit(null);
            paramVo.setRemark(null);
        }
        field.setAccessible(false);
    }

    /**
     * 转大写.
     * 
     * @param paramVo
     *            paramVo
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void dealToUpper(ParamVo paramVo, Annotation an) throws Exception {
        Field field = paramVo.getField();
        Object obj = paramVo.getObj();
        Class type = field.getType();
        Object value = field.get(obj);
        if (String.class.equals(type)) {
            if (null != value) {
                field.set(obj, value.toString().toUpperCase());
            }
        }
    }

    /**
     * 转小写.
     * 
     * @param paramVo
     *            paramVo
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void dealToLower(ParamVo paramVo, Annotation an) throws Exception {
        Field field = paramVo.getField();
        Object obj = paramVo.getObj();
        Object value = field.get(obj);
        Class type = field.getType();
        if (String.class.equals(type)) {
            if (null != value) {
                field.set(obj, value.toString().toLowerCase());
            }
        }
    }

    /**
     * 处理数字.
     * 
     * @param paramVo
     *            paramVo
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void dealNumber(ParamVo paramVo, Annotation an) throws Exception {
        Number number = (Number) an;
        Object over = null;
        Object dv = null;
        boolean isNumber = false;
        if (byte.class.equals(paramVo.getClaType()) || Byte.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Byte.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Byte.valueOf(String.valueOf(number.overstep()));
            }
        } else if (short.class.equals(paramVo.getClaType()) || Short.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Short.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Short.valueOf(String.valueOf(number.overstep()));
            }
        } else if (int.class.equals(paramVo.getClaType()) || Integer.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Integer.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Integer.valueOf(String.valueOf(number.overstep()));
            }
        } else if (long.class.equals(paramVo.getClaType()) || Long.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Long.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Long.valueOf(String.valueOf(number.overstep()));
            }
        } else if (float.class.equals(paramVo.getClaType()) || Float.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Float.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Float.valueOf(String.valueOf(number.overstep()));
            }
        } else if (double.class.equals(paramVo.getClaType()) || Double.class.equals(paramVo.getClaType())) {
            isNumber = true;
            if (null != number.defaultValue()) {
                dv = Double.valueOf(String.valueOf(number.defaultValue()));
            }
            if (null != number.overstep()) {
                over = Double.valueOf(String.valueOf(number.overstep()));
            }
        }
        if (isNumber) {
            if (null == paramVo.getValue()) {
                paramVo.getField().set(paramVo.getObj(), dv);
            } else {
                double d = Double.valueOf(String.valueOf(paramVo.getValue()));
                if (d > number.max() || d < number.min()) {
                    paramVo.getField().set(paramVo.getObj(), over);
                }
            }
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 必须为null.
     * 
     * @param paramVo
     *            paramVo
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNull(ParamVo paramVo, Annotation an) throws Exception {
        Null anIns = (Null) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (paramVo.getClaType().isPrimitive()) {
            buildException(paramVo, an);
        }
        if (null != paramVo.getField()) {
            checkParam(paramVo, true);
        }
    }

    /**
     * 非空非空字符串校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNotNullAndEmpty(ParamVo paramVo, Annotation an) throws Exception {
        NotNullAndEmpty anIns = (NotNullAndEmpty) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (String.class.equals(paramVo.getClaType())) {
            if (null == paramVo.getValue() || "".equals(paramVo.getValue().toString().trim())) {
                logToList(paramVo);
            }
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 座机号码校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateTel(ParamVo paramVo, Annotation an) throws Exception {
        Tel anIns = (Tel) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        paramVo.setRemark(Tel.regex);
        if (String.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !String.valueOf(paramVo.getValue()).matches(Tel.regex));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 邮编校验.
     * 
     * @param paramVo
     *            结果
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validatePostCode(ParamVo paramVo, Annotation an) throws Exception {
        Postcode anIns = (Postcode) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (String.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !String.valueOf(paramVo.getValue()).matches(Postcode.regex));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 电话（手机号）校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validatePhone(ParamVo paramVo, Annotation an) throws Exception {
        Phone anIns = (Phone) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        paramVo.setRemark(Phone.regex);
        if (String.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !String.valueOf(paramVo.getValue()).matches(Phone.regex));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 正则匹配校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validatePattern(ParamVo paramVo, Annotation an) throws Exception {
        Pattern anIns = (Pattern) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        paramVo.setRemark(anIns.value());
        if (String.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !String.valueOf(paramVo.getValue()).matches(anIns.value()));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 校验字符串长度.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateStringLength(ParamVo paramVo, Annotation an) throws Exception {
        StringLength anIns = (StringLength) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        paramVo.setRemark("[" + anIns.min() + "," + anIns.max() + "]");
        if (String.class.equals(paramVo.getClaType())) { // null.length()//Exception
            if (paramVo.isNotNull()) {
                if (null == paramVo.getValue() || String.valueOf(paramVo.getValue()).length() < anIns.min() || String.valueOf(paramVo.getValue()).length() > anIns.max()) {
                    logToList(paramVo);
                }
            } else {
                if (null != paramVo.getValue() && (String.valueOf(paramVo.getValue()).length() < anIns.min() || String.valueOf(paramVo.getValue()).length() > anIns.max())) {
                    logToList(paramVo);
                }
            }
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 校验是否是过去的时间.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validatePastTime(ParamVo paramVo, Annotation an) throws Exception {
        Date date = (Date) paramVo.getValue();
        PastTime anIns = (PastTime) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (Date.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, date.getTime() >= localTime.get());
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 检测参数并记录错误信息.
     * 
     * @param paramVo
     * @param bool
     */
    private static void checkParam(ParamVo paramVo, boolean bool) {
        if ((paramVo.isNotNull())) {
            if (null == paramVo.getValue() || bool) {
                logToList(paramVo);
            }
        } else {
            if (null != paramVo.getValue() && bool) {
                logToList(paramVo);
            }
        }
    }

    private static void logToList(ParamVo paramVo) {
        String log = paramVo.getClaName() + "[" + paramVo.getFieldName() + "]" + paramVo.getParamLimit();
        if (null != paramVo.getRemark()) {
            log += paramVo.getRemark();
        }
        localList.get().add(log);
    }

    /**
     * 注解的作用类型有误.
     * 
     * @param paramVo
     * @param an
     * @throws Exception
     */
    private static void buildException(ParamVo paramVo, Annotation an) throws Exception {
        throw new Exception(paramVo.getClaName() + "[" + paramVo.getFieldName() + "]注解[" + an.annotationType().getName() + "]作用类型有误,仅允许用于" + paramVo.getScope());
    }

    /**
     * 校验未来时间.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateFutureTime(ParamVo paramVo, Annotation an) throws Exception {
        Date date = (Date) paramVo.getValue();
        FutureTime anIns = (FutureTime) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (Date.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, date.getTime() <= localTime.get());
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * email校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateEmail(ParamVo paramVo, Annotation an) throws Exception {
        Email anIns = (Email) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        paramVo.setRemark(Email.regex);
        if (String.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !String.valueOf(paramVo.getValue()).matches(Email.regex));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * true校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateAssertTrue(ParamVo paramVo, Annotation an) throws Exception {
        AssertTrue anIns = (AssertTrue) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (boolean.class.equals(paramVo.getClaType()) || Boolean.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, !Boolean.parseBoolean(String.valueOf(paramVo.getValue())));
        } else { // boolean/Boolean
            buildException(paramVo, an);
        }
    }

    /**
     * false校验.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateAssertFalse(ParamVo paramVo, Annotation an) throws Exception {
        AssertFalse anIns = (AssertFalse) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (boolean.class.equals(paramVo.getClaType()) || Boolean.class.equals(paramVo.getClaType())) {
            checkParam(paramVo, Boolean.parseBoolean(String.valueOf(paramVo.getValue())));
        } else {
            buildException(paramVo, an);
        }
    }

    /**
     * 参数不能为null.
     * 
     * @param paramVo
     *            paramVo
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNotNull(ParamVo paramVo, Annotation an) throws Exception {
        NotNull anIns = (NotNull) an;
        initScopeParamLimit(paramVo, anIns.scope(), anIns.paramLimit());
        if (paramVo.getClaType().isPrimitive()) {
            buildException(paramVo, an);
        }
        if (null == paramVo.getValue()) {
            logToList(paramVo);
        }
    }

    /**
     * 初始化real处理的参数
     * 
     */
    private static ParamVo initDealParam(Object obj, Field field) throws Exception {
        ParamVo vo = new ParamVo();
        vo.setClaType(field.getType());
        vo.setClaName(obj.getClass().getName());
        vo.setFieldName(field.getName());
        vo.setValue(field.get(obj));
        vo.setField(field);
        vo.setObj(obj);
        vo.setNotNull(field.isAnnotationPresent(NotNull.class)); // field是否存在NotNull注解
        return vo;
    }

    private static void initScopeParamLimit(ParamVo paramVo, String scope, String paramLimit) {
        paramVo.setScope(scope);
        paramVo.setParamLimit(paramLimit);
    }
}

@SuppressWarnings("rawtypes")
class ParamVo {
    private Class claType;

    private String claName;

    private String fieldName;

    private Object value;

    private Object obj;

    private Field field;

    private boolean isNotNull;

    private String scope; // 作用域

    private String paramLimit; // 参数限制

    private String remark; // 其他说明

    public Class getClaType() {
        return claType;
    }

    public void setClaType(Class claType) {
        this.claType = claType;
    }

    public String getClaName() {
        return claName;
    }

    public void setClaName(String claName) {
        this.claName = claName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getParamLimit() {
        return paramLimit;
    }

    public void setParamLimit(String paramLimit) {
        this.paramLimit = paramLimit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}