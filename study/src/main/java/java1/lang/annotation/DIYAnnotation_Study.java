package java1.lang.annotation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import java1.lang.annotation.validation.ValidationUtil;

/**
 * 自定义注解，参数验证
 * 
 * @author zxiaofan
 */
@SuppressWarnings("unchecked")
public class DIYAnnotation_Study {
    Gson gson = new Gson();

    @Test
    public void testDIYAnno() {
        AnnoVo vo = new AnnoVo();
        String param = "I am about to change Upper";
        AnnoVo validate = null;
        try {
            vo.setGuestEmail("hi@zxiaofan.com");
            vo.setToUpper(param);
            vo.setBoolFalse(false); // true将报错,java.lang.AssertionError: expected null, but was:<参数:[boolFalse]只能为false>
            validate = (AnnoVo) ValidationUtil.dealParam(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // assertNull(validate); // 为空则参数正确
        assertEquals(param.toUpperCase(), vo.getToUpper());
    }

    @Test
    public void testStringCutBasic() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        vo.setCutName(param);
        vo.setGuestEmail("email");
        ValidationUtil.dealParam(vo);
        assertEquals(vo.getCutName().length(), 5);
    }

    @Test
    public void testStringCutListSet() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        // List转换
        List<AnnoVo> list = new ArrayList<>();
        vo.setCutName(param);
        list.add(vo);
        AnnoVo vo2 = new AnnoVo();
        vo2.setCutName("234567");
        list.add(vo2);
        list = (List<AnnoVo>) ValidationUtil.dealParam(list);
        String listvo = gson.toJson(list);
        System.out.println(listvo);
        // Set
        Set<AnnoVo> set = new HashSet<>();
        set.add(vo);
        set.add(vo2);
        set = (Set<AnnoVo>) ValidationUtil.dealParam(set);
        System.out.println(gson.toJson(set));
        Set<String> set2 = new HashSet<>();
        set2.add("set1");
        set2.add("set2");
        set2 = (Set<String>) ValidationUtil.dealParam(set2);
        System.out.println(gson.toJson(set2));

        // Map
        Map<String, AnnoVo> map = new HashMap<>();
        map.put("k1", vo);
        map.put("k2", vo2);
        map = (Map<String, AnnoVo>) ValidationUtil.dealParam(map);
        System.out.println(gson.toJson(map));
        // 复杂map
        Map<List<String>, List<AnnoVo>> map2 = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        list1.add("111");
        List<AnnoVo> list2 = new ArrayList<>();
        vo.setCutName("123456");
        vo.setGuestEmail("email");
        list2.add(vo);
        map2.put(list1, list2);
        map2 = (Map<List<String>, List<AnnoVo>>) ValidationUtil.dealParam(map2);
        System.out.println(gson.toJson(map2));
        System.out.println(ValidationUtil.validate(map2));
    }

    @Test
    public void testStringCutGeneric() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        // 泛型转换
        Request<AnnoVo> request = new Request<>();
        vo.setCutName(param);
        vo.setGuestEmail("com");
        request.setObj(vo);
        request = (Request<AnnoVo>) ValidationUtil.dealParam(request);
        System.out.println(gson.toJson(request));
        System.out.println(ValidationUtil.validate(request));

        // 复杂泛型
        Request<List<AnnoVo>> request2 = new Request<>();
        List<AnnoVo> list = new ArrayList<>();
        vo.setCutName("123456");
        vo.setGuestEmail("email");
        list.add(vo);
        request2.setObj(list);
        request2 = (Request<List<AnnoVo>>) ValidationUtil.dealParam(request2);
        System.out.println(gson.toJson(request2));
        System.out.println(ValidationUtil.validate(request2));
    }

    @Test
    public void testFuzaList() throws Exception {
        Request<AnnoListVo> request = initRequestVo();
        request = (Request<AnnoListVo>) ValidationUtil.dealParam(request);
        System.out.println(gson.toJson(request));
    }

    private Request<AnnoListVo> initRequestVo() {
        Request<AnnoListVo> request = new Request<>();
        AnnoListVo listAnno = new AnnoListVo();
        List<AnnoVo> listAnnovos = new ArrayList<>();
        AnnoVo vo = new AnnoVo();
        String param = "02883336666";
        vo.setCutName(param);
        vo.setGuestEmail("email@哈哈.com");
        vo.setNum(22);
        vo.setDate(new Date());
        listAnnovos.add(vo);
        listAnno.setListAnnovos(listAnnovos);
        request.setObj(listAnno);
        return request;
    }

    @Test
    public void testValidate() throws Exception {
        Request<AnnoListVo> request = initRequestVo();
        // String result = ValidationUtil.validate(request);
        request = (Request<AnnoListVo>) ValidationUtil.dealParam(request);
        System.out.println(gson.toJson(request));
    }
}
