package other;

public class CopyOfIfElse_TernaryOperator {

    public static void main(String[] args) {
        double f = 0;
        final int times = 100;
        double min = 1000;
        double max = 0;
        // 取100次平均。其实可以再多，但是耗时太长了，意义并不是很大。
        for (int i = 0; i < times; i++) {
            double df = test();
            System.out.println("df[" + i + "] = " + df);
            f += df;
            if (min > df) {
                min = df;
            }
            if (max < df) {
                max = df;
            }
        }
        f /= times;
        System.out.println("------->min = " + min);
        System.out.println("------->max = " + max);
        System.out.println("------->ave = " + f);
    }

    private static double test() {
        // 来点计算，用处不大。
        long sum = -1;
        // 循环次数，有点大哦，自己悠着点调
        final int max = 100000000;
        // 下面给出一个包含循环和计算的耗时统计，作为一个基础。后面的统计必然包含此部分时间
        long timeS = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            sum = i + i;
        }
        long timeE = System.currentTimeMillis();
        long delta = timeE - timeS;

        // 计算出一个布尔值，免得后面还要计算而占用时间，造成误差。
        boolean flag = sum > 0;
        // 下面是If/Else（包含循环和计算）的耗时统计，真实耗时应当除去上面的基准值
        long timeS1 = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            if (flag) {
                sum = i + i;
            } else {
                sum = i + i;
            }
        }
        long timeE1 = System.currentTimeMillis();
        long delta1 = timeE1 - timeS1;
        // 这是真实的耗时计算
        long realIfElse = delta1 - delta;

        // 下面是三目运算符（包含循环和计算）的耗时统计，真实耗时同样应当除去前面的基准值
        long timeS2 = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            sum = flag ? i + i : i + i;
        }
        long timeE2 = System.currentTimeMillis();
        long delta2 = timeE2 - timeS2;
        // 这是真实的耗时计算
        long realTri = delta2 - delta;

        // 返回耗时比率，以反映效率差别
        double f = (double) realIfElse / realTri;
        return f;
    }
}