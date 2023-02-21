package com.teapot.chapter6.kmeans;

import java.util.ArrayList;
import java.util.List;

public class DataPoint {
    public final int numDimensions;
    private final List<Double> originals;
    // 存储每个维度的实际值
    public List<Double> dimensions;

    public DataPoint(List<Double> initials) {
        originals = initials;
        dimensions = new ArrayList<>(initials);
        numDimensions = dimensions.size();
    }

    /**
     * 计算欧式距离
     * @param other
     * @return
     */
    public double distance(DataPoint other) {
        double differences = 0.0;
        for (int i = 0; i < numDimensions; i++) {
            double difference = dimensions.get(i) - other.dimensions.get(i);
            differences += Math.pow(difference, 2);
        }
        return Math.sqrt(differences);
    }

    @Override
    public String toString() {
        return originals.toString();
    }
}
