/*
 * 文件名：Predicate_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Predicate_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月29日
 * 修改内容：新增
 */
package java1.util.function;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Java 8 Predicate
 * 
 * @author yunhai
 */
public class Predicate_Study {

    @Test
    public void OperationCol() {
        Collection<String> books = new HashSet<String>();
        books.add("疯狂Java讲义");
        books.add("轻量级JavaEE企业应用实践");
        books.add("剑指offer 名企面试官精讲典型编程题");
        books.add("数据结构（C++版）习题解答与实验指导");
        books.add("RedBlack");
        books.add("HashMap源码分析");
        books.add("test");
        books.removeIf(book -> book.length() < 5); // remove了"test"
        System.out.println(books);
        System.out.println(calAll(books, book -> ((String) book).contains("Java"))); // 2
        System.out.println(books.stream().filter(book -> book.contains("Java")).count()); // 2
    }

    @Test
    public void OperationModle() {
        List<Student> listStu = new ArrayList<Student>();
        listStu.add(new Student("A", 20));
        listStu.add(new Student("B", 30));
        listStu.add(new Student("C", 25));
        listStu.add(new Student("D", 20));
        System.out.println(((Student) myFilter(listStu, myPredicate).get(0)).getName());
    }

    /**
     * 谓词的直接使用.and/or待续
     */
    @Test
    public void other() {
        final Predicate<Integer> isZero = Predicate.isEqual(0);
        final Predicate<Integer> isnull = Predicate.isEqual(null);
        final Predicate iszeroOrnull = isZero.or(isnull);
        assertTrue(isZero.test(0));
        // assertTrue(isZero.test(1));
        // assertTrue(isnull.test("a"));
        assertTrue(isnull.test(null));
        assertTrue(iszeroOrnull.test(1));
    }

    /**
     * 统计满足Predicate的元素个数.
     */
    private int calAll(Collection books, Predicate pre) {
        int total = 0;
        for (Object object : books) {
            if (pre.test(object)) {
                total++;
            }
        }
        return total;
    }

    Predicate myPredicate = new Predicate() {
        @Override
        public boolean test(Object object) {
            if (object instanceof Student) {
                Student resource = (Student) object;
                String name = null;
                Integer age = 25;
                if (name == null) {
                    if (resource.getAge() == age) {
                        return true;
                    }
                } else if (age == null) {
                    if (resource.getName() == name) {
                        return true;
                    }
                } else {
                    if (resource.getAge() == age && resource.getName() == name) {
                        return true;
                    }
                }

            }
            return false;
        }
    };

    public List myFilter(List<Student> listStu, Predicate pre) {
        return (List) listStu.stream().filter(pre).collect(Collectors.toList());
    }

    class Student {
        private String name;

        private int age;

        public Student(String name, int age) {
            super();
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
