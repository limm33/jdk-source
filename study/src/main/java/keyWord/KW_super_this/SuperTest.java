/*
 * 文件名：KW_super_this.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： KW_super_this.java
 * 修改人：yunhai
 * 修改时间：2015年12月18日
 * 修改内容：新增
 */
package keyWord.KW_super_this;

import org.junit.Test;

/**
 * 若没有显示指定调用者，查找变量a的顺序为：
 * 
 * 1、当前方法名为a的局部变量；
 * 
 * 2、当前类名为a的成员变量；
 * 
 * 3、a的直接父类是否包含名为a的成员变量，一次上溯a的所有父类，直到Object，若找不到，编译错误。
 * 
 * 注：子类不能调用父类中private的变量或方法。
 * 
 * @author yunhai
 */
class Base {
    public String str = "str-Base";

    String strOnlyBase = "strOnlyBase，子类中没有此变量，无需用super或父类名调用";

    public void Method() {
        System.out.println("Method-Base");
    }
}

class Hello extends Base {
    private String str = "str-Hello-private,不能直接被调用，可向上转型为父类调用";
}

public class SuperTest extends Base {

    public SuperTest() {
    }

    String str = "str-Son";

    @Test
    public void test() {
        SuperTest kw = new SuperTest();
        System.out.println(str); // str-Son
        System.out.println(super.str); // str-Base
        System.out.println(strOnlyBase); // strOnlyBase，子类中没有此变量，无需用super或父类名调用
        Hello hello = new Hello();
        // System.out.println(hello.str); // 编译错误
        System.out.println(((Base) hello).str); // 输出的是父类的变量，而不是Hello的
    }
}
