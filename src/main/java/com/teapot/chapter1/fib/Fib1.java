package com.teapot.chapter1.fib;

/**
 * P1
 * 使用递归方法计算斐波那契数列
 * 由于没有设置基线条件，出现无限递归
 */
public class Fib1 {
    private static int fib1(int n) {
        return fib1(n - 1) + fib1(n -2);
    }

    public static void main(String[] args) {
        System.out.println(fib1(5));
    }
}
