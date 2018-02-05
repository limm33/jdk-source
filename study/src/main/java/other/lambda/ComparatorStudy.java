package other.lambda;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 多条件排序测试。
 * 
 * level较大的排前面；level相同，Salary较大的排前面；Salary相同，name字母靠前的排前面
 */
public class ComparatorStudy {
    public static void main(String[] args) {
        testComparator();
        System.out.println();
        testComparable();
    }

    private static void testComparator() {
        List<Person> objs = new ArrayList<Person>() {
            {
                add(new Person("aa", 3, 5000));
                add(new Person("b", 9, 10000));
                add(new Person("ccc", 5, 8000));
                add(new Person("dd", 9, 12000));
                add(new Person("eeee", 1, 2000));
                add(new Person("ff", 5, 8000));
            }
        };
        Collections.sort(objs, comparator);
        System.out.println("Name\tLevel\tSalary\n=============================");
        for (Person a : objs)
            System.out.printf("%s\t%d\t%d\n", a.name, a.level, a.salary);
    }

    /**
     * 测试Comparable
     */
    private static void testComparable() {
        // User[] users = new User[]{new User("a", 30), new User("b", 20), new User("c", 25), new User("d", 55)};
        User[] users2 = new User[]{new User("aa", 30), new User("bb", 20)};
        // Arrays.sort(users);
        Arrays.sort(users2);
        // for (int i = 0; i < users.length; i++) {
        // User user = users[i];
        // System.out.println(user.getId() + " " + user.getAge());
        // }
        Arrays.stream(users2).forEach(p -> System.out.print(p.id + "," + p.age + "----"));
    }

    private final static Comparator<Person> comparator = new Comparator<Person>() {
        @Override
        public int compare(Person o2, Person o1) {// o2下标i+1，o1下标i
            int cr = 0;
            int a = o1.level - o2.level;
            if (a != 0)
                cr = (a > 0) ? 1 : -1; // 只区分正负,结果为负数，第一个参数o2靠后；0则保持原有顺序。(序反，降序)？？？
            else {
                a = o1.salary - o2.salary;
                if (a != 0)
                    cr = (a > 0) ? 1 : -1;
                else {
                    cr = o2.name.compareTo(o1.name);
                }
            }
            return cr;
        }
    };

    private static class User implements Comparable<Object> {
        private String id;
        private int age;

        public User(String id, int age) {
            this.id = id;
            this.age = age;
        }
        public int getAge() {
            return age;
        }
        public String getId() {
            return id;
        }
        @Override
        public int compareTo(Object o) { // 重写compareTo方法，按age比较,第一次的o为第一个元素aa,30
            String str = "";
            int k = this.age - ((User) o).getAge(); // 第一次this位第二个元素bb,20
            str = k > 0 ? ">" : "<";
            System.out.println(this.id + str + ((User) o).getId());// c、b比较了两次；内部如何比较排序？
            return k;
        }
    }

}