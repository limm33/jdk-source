/*
 * 文件名：GoTo.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： GoTo.java
 * 修改人：zxiaofan
 * 修改时间：2017年11月12日
 * 修改内容：新增
 */
package other;

/**
 * 【循环标签】Java中的“goto”实现.
 * 
 * C/C++等语言中，使用goto可以实现程序的跳转，从某些方面来说其提供了一定的方便性，但不仅降低程序的可读性，也会对程序的维护与更新造成影响。
 * 
 * Java语言取消了goto的使用，取而代之的是使用循环标签。
 * 
 * 为了避免程序员自行使用goto带来同样的混乱性，Java语言仍将goto定义为一个关键字，用来限制程序员将goto作为一个标识符来使用，由于是一个从不使用的关键字，故也称为“保留字”。
 * 
 * @author zxiaofan
 */
public class GoTo {

    public static void main(String[] args) {
        int i = 0;
        int j = 0;
        retry1: for (; i < 3; i++) {
            retry2: for (; j < 4; j++) {
                System.out.println("___j___:" + j);
                if (j == 1) {
                    System.out.println("j:" + j + ">> continue retry2");
                    // continue retry1;
                    continue retry2; // 跳转到retry2
                }
                if (j >= 1) {
                    System.out.println("j:" + j + ">> break retry2");
                    break retry2; // 结束当前循环retry2
                }
            }
            if (i > 1) {
                System.out.println("i:" + i + ">> continue retry1");
                // continue retry2; // 异常：The label retry2 is missing
                continue retry1; // 跳转到retry1
            }
            System.out.println("End_i:" + i);
        }
    }
}
