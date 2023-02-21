package com.teapot.chapter7.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * P136~146
 * 工具包
 */
public class Util {
    /**
     * 点积
     */
    public static double dotProduct(double[] xs, double[] ys) {
        double sum = 0.0;
        for (int i = 0; i < xs.length; i++) {
            sum += xs[i] * ys[i];
        }
        return sum;
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * 激活函数
     */
    public static double derivativeSigmoid(double x) {
        double sig = sigmoid(x);
        return sig * (1.0 - sig);
    }

    public static void normalizeByFeatureScaling(List<double[]> dateset) {
        for (int colNum = 0; colNum < dateset.get(0).length; colNum++) {
            List<Double> colnum = new ArrayList<>();
            for (double[] row: dateset) {
                colnum.add(row[colNum]);
            }
            double maximum = Collections.max(colnum);
            double minimum = Collections.min(colnum);
            double difference = maximum - minimum;
            for (double[] row: dateset) {
                row[colNum] = (row[colNum] - minimum) / difference;
            }
        }
    }

    public static List<String[]> loadCSV(String fileName) {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(fileName)){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().map(line -> line.split(",")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static double max(double[] numbers) {
        return Arrays.stream(numbers).max().orElse(Double.MIN_VALUE);
    }
}
