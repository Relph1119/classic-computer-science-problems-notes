package com.teapot.chapter6.kmeans;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * P113
 * 基本统计方法
 */
public final class Statistics {
    private final List<Double> list;
    private final DoubleSummaryStatistics dss;

    public Statistics(List<Double> list) {
        this.list = list;
        dss = list.stream().collect(Collectors.summarizingDouble(d -> d));
    }

    public double sum() {
        return dss.getSum();
    }

    public double mean() {
        return dss.getAverage();
    }

    public double variance() {
        double mean = mean();
        return list.stream().mapToDouble(x -> Math.pow((x - mean), 2)).average().getAsDouble();
    }

    public double std() {
        return Math.sqrt(variance());
    }

    /**
     * 计算z-score
     * z-score = (x - mean) / std
     */
    public List<Double> zscored() {
        double mean = mean();
        double std = std();
        return list.stream().map(x -> std != 0 ? ((x - mean) / std) : 0.0).collect(Collectors.toList());
    }

    public double max() {
        return dss.getMax();
    }

    public double min() {
        return dss.getMin();
    }
}