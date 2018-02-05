/*
 * 文件名：BlockingQueue_Study.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： BlockingQueue_Study.java
 * 修改人：zxiaofan
 * 修改时间：2017年4月11日
 * 修改内容：新增
 */
package java1.util.concurrent.blockingQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Test;

/**
 * @author zxiaofan
 */
public class BlockingQueue_Study {
    BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>(5); // <队列对象String>(队列容量)

    List<String> list = Arrays.asList("list1", "list2");

    /**
     * 插入操作：插入元素为null时将抛出NullPointerException.
     * 
     */
    @Test
    public void BlockingQueueInsertTest() {
        // add-AbstractQueue
        boolean add = blockingQueue.add("add"); // 插入成功：true；阻塞队列已满：抛出java.lang.IllegalStateException: Queue full
        System.out.println("add:" + add);
        boolean offer = blockingQueue.offer("offer"); // 插入成功:true；队列已满:false；
        System.out.println("offer:" + offer);
        try {
            blockingQueue.put("put"); // 无返回值；一直阻塞直到插入成功；线程被中断即Thread.interrupted()时：抛出InterruptedException
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            boolean offer_timeout = blockingQueue.offer("offer_timeout", 3, TimeUnit.SECONDS); // 插入成功：true；插入超时：false；线程被中断即Thread.interrupted()时：抛出InterruptedException
            System.out.println("offer_timeout:" + offer_timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean addAll = false;
        try {
            // addAll-AbstractQueue
            addAll = blockingQueue.addAll(list); // 插入成功：true；内部结构为foreach遍历add(e)，所以可能部分数据插入成功，部分插入失败
        } catch (Exception e) { // 吃掉异常
            System.err.println("addAll_Exception:" + e.getMessage());
            // e.printStackTrace();
        }
        System.out.println("addAll:" + addAll);
        System.out.println(blockingQueue.toString());
    }

    /**
     * 阻塞队列移除操作.
     * 
     */
    @Test
    public void BlockingQueueRemoveTest() {
        BlockingQueueInsertTest();
        // remove-AbstractQueue
        String remove = blockingQueue.remove(); // 移除队列头。移除成功：返回被移除的元素；队列为空：抛出NoSuchElementException
        System.out.println("remove:" + remove);
        boolean removeObj = blockingQueue.remove("offer"); // 用equals()判断两个对象是否相等。移除成功：true；不存在该元素：false
        System.out.println("removeObj:" + removeObj);
        String poll = blockingQueue.poll(); // 移除队列头。和remove()的唯一区别在于，当队列为空返回null而不是抛异常
        System.out.println("poll:" + poll);
        try {
            String poll_timeout = blockingQueue.poll(3, TimeUnit.SECONDS); // 移除成功：返回被移除的元素；移除超时（一直无可移除的对象）：返回null；线程被中断抛InterruptedException
            System.out.println("poll_timeout:" + poll_timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            String take = blockingQueue.take(); // 一直阻塞，直到取回数据；等待时被中断则抛出InterruptedException
            System.out.println("take:" + take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blockingQueue.add("list1");
        boolean removeAll = blockingQueue.removeAll(list); // Iterator<?> it = iterator();迭代remove()，只要remove成功一个且不抛异常就返回false
        System.out.println("removeAll:" + removeAll);
        System.out.println(blockingQueue.toString());
        blockingQueue.addAll(list);
        Predicate<? super String> filter = (p) -> p.startsWith("list");
        boolean removeIf = blockingQueue.removeIf(filter); // JDK8可用；remove成功任何一个元素即返回true
        System.out.println("removeIf:" + removeIf);
        System.out.println(blockingQueue.toString());
        //
        System.out.println("数据保留");
        BlockingQueueInsertTest();
        // retailAll-AbstractCollection
        boolean retainAll = blockingQueue.retainAll(list); // 队列因此请求而改变则返回true，没有remove任何一个元素即返回false
        System.out.println("retainAll:" + retainAll);
        System.out.println(blockingQueue.retainAll(Arrays.asList("retailAll")));
        System.out.println(blockingQueue.retainAll(list)); // 没有remove任何一个元素即返回false
        System.out.println(blockingQueue.toString());
        blockingQueue.clear(); // 移除所有元素，clear首先加锁fullyLock()
    }

    /**
     * 阻塞队列的cheek：element()/peek().
     * 
     */
    @Test
    public void BlockingQueueCheckTest() {
        BlockingQueueInsertTest();
        String element = blockingQueue.element(); // 取回但不移除队首元素，队列为空throw NoSuchElementException
        System.out.println("element:" + element);
        String peek = blockingQueue.peek(); // 取回但不移除队首元素，队列为空返回null
        System.out.println("peek:" + peek);
        boolean contains = blockingQueue.contains("add"); // 有一元素equals即返回true
        System.out.println("contains:" + contains);
        boolean containsAll = blockingQueue.containsAll(Arrays.asList("add", "offerNew")); // contains所有元素即返回true
        System.out.println("containsAll:" + containsAll);
        boolean isEmpty = blockingQueue.isEmpty(); // 队列无元素即返回true
        System.out.println("isEmpty:" + isEmpty);
    }

    @Test
    public void BlockingQueueOtherTest() {
        BlockingQueueInsertTest();
        // 容量
        System.out.println("size:" + blockingQueue.size());
        int remainingCapacity = blockingQueue.remainingCapacity();
        System.out.println("remainingCapacity:" + remainingCapacity);
        // 遍历
        Consumer<? super String> consumer = e -> e = e + "_New"; // 将所有元素拼接一个"_New",实际blockingQueue队列元素并为改变，因为String具有不可变性
        blockingQueue.forEach(consumer); // JDK8
        System.out.println("consumer:" + blockingQueue.toString());
        blockingQueue.forEach(e -> {
            System.out.println(e + "_New"); // 遍历每个元素拼接"_New"并打印
        });
        int sum = blockingQueue.parallelStream().filter(p -> p.length() > 4).mapToInt(p -> p.length()).sum();
        System.out.println("JDK8流处理（字符串长度大于4的元素的字符长度和）:" + sum);
        blockingQueue.spliterator().forEachRemaining(e -> {
            System.out.println(e + "_FER");
        }); // forEachRemaining：JDK8迭代器默认方法，为集合所有未处理元素执行Action
        // drainTo
        // 比重复poll更高效。drainTo操作时会加锁（ReentrantLock），add操作将等待，故正如Java Doc描述drainTo(Collection c)会将[所有]可用元素加入集合c。
        int actualSize = blockingQueue.size() - blockingQueue.remainingCapacity(); // 队列实际元素个数
        List<String> listDrain = new ArrayList<String>(actualSize);
        new Thread(new Runnable() {

            @Override
            public void run() {
                blockingQueue.add("drainToNew"); // 调试时在此行加断点
            }
        }).start();
        int drainTo = blockingQueue.drainTo(listDrain); // 此行断点调试内部
        // listDrain、blockingQueue都可能有值（并非drainTo未移除所有元素，而是drainTo后又add了新元素）
        System.out.println("drainTo:" + drainTo + ",listDrain:" + listDrain.toString() + ",blockingQueue:" + blockingQueue.toString());
        BlockingQueueInsertTest();
        listDrain.clear();
        int drainToMax = blockingQueue.drainTo(listDrain, 3); // 移除最多max个元素并将其加入listDrain
        System.out.println("drainToMax:" + drainToMax + ",listDrain:" + listDrain.toString() + ",blockingQueue:" + blockingQueue.toString());
        Object[] toArray = blockingQueue.toArray(); // 返回包含集合所有元素的数组，数组顺序和原有集合一致。返回的数组存在于新的内存空间，和原有队列的修改操作不会互相影响。
        System.out.println("toArray:" + Arrays.asList(toArray).toString());
        String[] arrToArray = new String[]{"arr1", "arr2", "arr3"}; // 待储存集合空间充足，将队列所有元素按序存于数组，并将多余元素置为null
        String[] toArrayT = blockingQueue.toArray(arrToArray);
        System.out.println("toArrayT:" + Arrays.asList(toArrayT).toString());
        String[] arrToArray2 = new String[]{"arr1"}; // 待储存集合空间不足，将队列所有元素按序存于【新的数组】
        String[] toArrayT2 = blockingQueue.toArray(arrToArray2);
        System.out.println("toArrayT2:" + Arrays.asList(toArrayT2).toString());
    }
}
