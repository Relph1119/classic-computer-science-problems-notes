package com.teapot.chapter1.pi;

/**
 * P13
 * 使用莱布尼兹公式计算pi
 * n = 4/1 - 4/3 + 4/5 - 4/7 + 4/9 - 4/11 ...
 * 分母逐项增加2，分子是4，对每一项的运算在加法和减法之间交替进行
 */
public class PiCalculator {
    public static double calculatePi(int nTimes) {
        final double numerator = 4.0;
        double denominator = 1.0;
        double operation = 1.0;
        double pi = 0.0;
        for (int i = 0; i < nTimes; i++) {
            pi += operation * (numerator / denominator);
            denominator += 2.0;
            operation *= -1.0;
        }
        return pi;
    }

    public static void main(String[] args) {
        System.out.println(calculatePi(1000000));
    }
}
