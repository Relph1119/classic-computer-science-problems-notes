package com.teapot.chapter1.hanoi;

import java.util.Stack;

/**
 * P14~16
 * 汉诺塔问题：
 * 使用栈模拟汉诺塔，使用递归方法
 */
public class Hanoi {
    private final int numDiscs;
    public final Stack<Integer> towerA = new Stack<>();
    public final Stack<Integer> towerB = new Stack<>();
    public final Stack<Integer> towerC = new Stack<>();

    public Hanoi(int discs) {
        numDiscs = discs;
        for (int i = 1; i <= discs; i++) {
            towerA.push(i);
        }
    }

    /**
     * 1. 将上面的n-1个圆盘从A塔移动到B塔（暂存塔），使用C塔作为中转塔
     * 2. 将最底层的圆盘从A塔移动到C塔
     * 3. 将n-1个圆盘从B塔移动到C塔，使用A塔作为中转塔
     *
     * @param begin 起始塔
     * @param end 终止塔
     * @param temp 暂存塔
     * @param n 移动的圆盘数量
     */
    private void move(Stack<Integer> begin, Stack<Integer> end, Stack<Integer> temp, int n) {
        if (n == 1) {
            end.push(begin.pop());
        } else {
            // begin为A塔，temp为B塔，end为C塔
            move(begin, temp, end, n - 1);
            move(begin, end, temp, 1);
            move(temp, end, begin, n - 1);
        }
    }

    public void solve() {
        move(towerA, towerC, towerB, numDiscs);
    }

    public static void main(String[] args) {
        Hanoi hanoi = new Hanoi(3);
        hanoi.solve();
        System.out.println(hanoi.towerA);
        System.out.println(hanoi.towerB);
        System.out.println(hanoi.towerC);
    }
}
