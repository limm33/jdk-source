/*
 * 文件名：ArrayDeque_Study.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： ArrayDeque_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月30日
 * 修改内容：新增
 */
package java1.util;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.junit.Test;

/**
 * ArrayDeque双端队列，既可作为队列又可作为栈
 * 
 * LinkedList实现了Deque接口，可被当成双端队列，既可作为队列又可作为栈
 * 
 * @author yunhai
 */
public class ArrayDeque_Study {

    /**
     * Queue【接口】：peek()、poll()、null.
     * 
     */
    @Test
    public void queue() {
        Queue queue = new LinkedList();
        queue.add("a");
        queue.offer("A");
        queue.add("b");
        System.out.println(queue.peek()); // a 返回队首元素
        System.out.println(queue); // [a, A, b]
        System.out.println(queue.poll()); // a 返回并移除队首元素
        System.out.println(queue); // [A, b]
        System.out.println((new LinkedList()).poll()); // null 队列空pool返回null
    }

    /**
     * LinkedList
     * 
     * 作为队列使用，FIFO,peek()、poll()、null.
     * 
     * 作为栈使用，FILO,peek()、pop()、EmptyStackException.
     */
    @Test
    public void LinkedList() {
        LinkedList queue = new LinkedList();
        queue.add("a");
        queue.offer("A");
        queue.add("b");
        System.out.println(queue.peek()); // a 返回队首元素
        System.out.println(queue); // [a, A, b]
        // LinkedList 作为队列使用，FIFO.
        System.out.println(queue.poll()); // a 返回并移除队首元素
        System.out.println(queue); // [A, b]
        System.out.println((new LinkedList()).poll()); // null 队列空pool返回null
        // LinkedList 作为栈使用，FILO.
        System.out.println(queue.pop()); // A 【栈的用法】
        System.out.println(queue); // [b]
        System.out.println((new LinkedList()).pop()); // 栈空pop抛异常EmptyStackException
    }

    /**
     * Stack【类】：peek()、pop()、EmptyStackException.
     */
    @Test
    public void stack() {
        Stack stack = new Stack();
        stack.add("a");
        stack.add(0, "A");
        stack.add("b");
        System.out.println(stack.peek()); // b 返回栈顶元素
        System.out.println(stack); // [A, a, b]
        System.out.println(stack.pop()); // b 返回并移除栈顶元素
        System.out.println(stack); // [A, b]
        System.out.println((new Stack()).pop()); // 栈空pop抛异常EmptyStackException
    }

    /**
     * ArrayDeque
     * 
     * 作为队列使用，FIFO,peek()、poll()、null.
     * 
     * 作为栈使用，FILO,peek()、pop()、EmptyStackException.
     */
    @Test
    public void asQueueOrStack() {
        ArrayDeque queue = new ArrayDeque();
        queue.add("a");
        queue.addFirst("A");
        queue.add("b");
        queue.addLast("B");
        System.out.println(queue.peek()); // A 返回队首元素
        System.out.println(queue.peekLast()); // B 返回队尾元素
        System.out.println(queue); // [A, a, b, B]
        // ArrayDeque 作为队列使用，FIFO.
        System.out.println(queue.poll()); // A 返回并移除队首元素
        System.out.println(queue); // [a, b, B]
        // ArrayDeque 作为栈使用，FILO.
        System.out.println(queue.pop()); // a 【栈的用法】
        System.out.println(queue); // [b, B]
        System.out.println((new ArrayDeque()).poll()); // null 队列空pool返回null
        System.out.println((new ArrayDeque()).pop()); // EmptyStackException 栈空pop抛异常
    }
}
