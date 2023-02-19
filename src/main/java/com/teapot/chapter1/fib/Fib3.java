package com.teapot.chapter1.fib;

import java.util.HashMap;
import java.util.Map;

/**
 * P4
 * 使用Java Map实现记忆化，类似于备忘录模式
 */
public class Fib3 {
    // 预先存放基线 0 -> 0、1 -> 1
    static Map<Integer, Integer> memo = new HashMap<>(Map.of(0, 0, 1, 1));

    private static int fib3(int n) {
        if (!memo.containsKey(n)) {
            memo.put(n, fib3(n - 1) + fib3(n -  2));
        }
        return memo.get(n);
    }

    public static void main(String[] args) {
        System.out.println(fib3(5));
        System.out.println(fib3(40));
    }
}
