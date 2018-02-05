package other.lambda;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Alin
 */
public class Lambda3 {

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {

        List<Person> list = new ArrayList<Person>() {
            {
                add(new Person("aa", 3, 5000, true));
                add(new Person("b", 9, 10000, false));
                add(new Person("ccc", 5, 8000, true));
                add(new Person("dd", 9, 12000, true));
                add(new Person("eeee", 1, 2000, false));
                add(new Person("ff", 7, 8000, false));
            }
        };
        System.out.println("========foeEach迭代输出==========");
        list.forEach((per) -> System.out.print(per.name + "\t"));
        System.out.println("\n========foeEach加薪==========");
        Consumer<Person> addSalary = e -> e.setSalary(e.getSalary() + 8);
        list.forEach(addSalary);
        list.forEach(per -> System.out.print(per.salary + "\t"));
        System.out.println("\n========foeEach加薪<一行代码>==========");
        list.stream().map(p -> (p.salary + 12)).forEach(System.out::println);// 没有（）
        System.out.println("\n========startsWith(f)==========");// 同理endsWith
        list.stream().filter(p -> p.name.startsWith("f")).forEach((p) -> System.out.print(p.name + "\t"));
        System.out.println("\n========salary>8000==========");
        list.stream().filter((p) -> (p.getSalary() > 8000)).forEach((p) -> System.out.print(p.salary + "\t"));
        System.out.println("\n========salary>8000 AND true==========");
        Predicate<Person> salaryFillter = (p) -> (p.salary > 8000); // 自定义Predicate供filter调用
        Predicate<Person> boFillter = (p) -> (p.bool == true);
        list.stream().filter(salaryFillter).filter(boFillter).forEach((p) -> System.out.print(p.name + "\t"));
        System.out.println("\n========list前3个中,salary>8000 AND true==========");
        list.stream().limit(3).filter(salaryFillter).filter(boFillter).forEach((p) -> System.out.print(p.name + "\t"));
        System.out.println("\n========list前3个salary>8000==========");
        list.stream().filter(salaryFillter).limit(3).forEach((p) -> System.out.print(p.name + "\t"));
        System.out.println("\n========name逆序，前4==========");
        List<Person> sortByNameList = list.stream().sorted((p, p1) -> (p1.name.compareTo(p.name))).limit(4).collect(Collectors.toList());
        sortByNameList.forEach((p) -> System.out.print(p.name + "\t"));
        System.out.println("\n========min和max==========");
        Person per = list.stream().min((p1, p2) -> (p1.salary - p2.salary)).get(); // 必须升序排列
        System.out.println(per.name);
        System.out.println("\n========collect方法来将结果集放到一个字符串,一个 Set 或一个TreeSet==========");
        String nameStr = list.stream().map(Person::getName).collect(Collectors.joining(" ; ")); // collect针对String；双目运算符 ？？
        // String nameStr = list.stream().map((p) -> p.name.toUpperCase()).collect(Collectors.joining(" ; "));
        System.out.println(nameStr);
        Map nameMap = list.stream().collect(Collectors.toMap(p1 -> p1.name, p1 -> p1.salary));
        System.out.println(nameMap.toString());
        System.out.println("\n========并行演示==========");// 并行，大量数据时提升效率
        int sumSa = list.parallelStream().filter((p) -> (p.bool == true)).mapToInt(p -> p.salary).sum();
        System.out.println("bool=true的salary总和：" + sumSa);// ==
        System.out.println("\n========各种汇总数据==========");
        IntSummaryStatistics sta = list.stream().mapToInt(p -> p.salary).summaryStatistics();
        System.out.println("salary.max:" + sta.getMax());
        System.out.println("salary.min:" + sta.getMin());
        System.out.println("salary.sum:" + sta.getSum());
        System.out.println("salary.Average:" + sta.getAverage());
        System.out.println("salary.Count:" + sta.getCount());
        System.out.println("salary.toString:" + sta.toString());
        // lambda代码不能自动补全！！！
    }
}
