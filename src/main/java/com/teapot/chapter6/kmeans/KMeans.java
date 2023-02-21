package com.teapot.chapter6.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * P116~121
 * k均值聚类
 * 1. 对所有数据点和k个空簇进行初始化
 * 2. 对所有数据点进行归一化处理
 * 3. 为每个簇创建其随机分布的形心
 * 4. 将每个数据点分配到与其距离最近的形心所在的簇中
 * 5. 重新进行计算，得到每个簇的新的形心位置
 * 6. 重复第4、5步，直到迭代次数达到最大值或所有形心都停止移动（收敛）
 */
public class KMeans<Point extends DataPoint> {
    public class Cluster {
        public List<Point> points;
        public DataPoint centroid;

        public Cluster(List<Point> points, DataPoint centroid) {
            this.points = points;
            this.centroid = centroid;
        }
    }

    private List<Point> points;
    private List<Cluster> clusters;

    public KMeans(int k, List<Point> points) {
        if (k < 1) {
            throw new IllegalArgumentException("k must be >= 1");
        }
        this.points = points;
        // 归一化处理
        zScoreNormalize();
        clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            // 随机形心
            DataPoint randPoint = randomPoint();
            Cluster cluster = new Cluster(new ArrayList<Point>(), randPoint);
            clusters.add(cluster);
        }
    }


    /**
     * 归一化处理
     */
    private void zScoreNormalize() {
        List<List<Double>> zscored = new ArrayList<>();
        for (Point point : points) {
            zscored.add(new ArrayList<Double>());
        }
        for (int dimension = 0; dimension < points.get(0).numDimensions; dimension++) {
            List<Double> dimensionSlice = dimensionSlice(dimension);
            Statistics stats = new Statistics(dimensionSlice);
            List<Double> zscores = stats.zscored();
            for (int index = 0; index < zscores.size(); index++) {
                zscored.get(index).add(zscores.get(index));
            }
        }
        for (int i = 0; i < points.size(); i++) {
            points.get(i).dimensions = zscored.get(i);
        }
    }

    /**
     * 随机形心
     */
    private DataPoint randomPoint() {
        List<Double> randDimensions = new ArrayList<>();
        Random random = new Random();
        for (int dimension = 0; dimension < points.get(0).numDimensions; dimension++) {
            List<Double> values = dimensionSlice(dimension);
            Statistics stats = new Statistics(values);
            // 在该范围区域内取随机值
            Double randValue = random.doubles(stats.min(), stats.max()).findFirst().getAsDouble();
            randDimensions.add(randValue);
        }
        return new DataPoint(randDimensions);
    }

    /**
     * 返回所有簇相关联的形心
     *
     * @return
     */
    private List<DataPoint> centroids() {
        return clusters.stream().map(cluster -> cluster.centroid).collect(Collectors.toList());
    }

    private List<Double> dimensionSlice(int dimension) {
        return points.stream().map(x -> x.dimensions.get(dimension)).collect(Collectors.toList());
    }

    /**
     * 为每个数据点找到合适的簇
     */
    private void assignClusters() {
        for (Point point : points) {
            double lowestDistance = Double.MAX_VALUE;
            Cluster closestCluster = clusters.get(0);
            for (Cluster cluster : clusters) {
                double centroidDistance = point.distance(cluster.centroid);
                // 得到距离最近的点
                if (centroidDistance < lowestDistance) {
                    lowestDistance = centroidDistance;
                    closestCluster = cluster;
                }
            }
            closestCluster.points.add(point);
        }
    }

    /**
     * 得到簇的形心
     */
    private void generateCentroids() {
        for (Cluster cluster : clusters) {
            if (cluster.points.isEmpty()) {
                continue;
            }
            List<Double> means = new ArrayList<>();
            for (int i = 0; i < cluster.points.get(0).numDimensions; i++) {
                int dimension = i;
                Double dimensionMean = cluster.points.stream().mapToDouble(x -> x.dimensions.get(dimension))
                        .average().getAsDouble();
                means.add(dimensionMean);
            }
            cluster.centroid = new DataPoint(means);
        }
    }

    private boolean listEqual(List<DataPoint> first, List<DataPoint> second) {
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < first.get(0).numDimensions; j++) {
                if(first.get(i).dimensions.get(j).doubleValue() != second.get(i).dimensions.get(j).doubleValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Cluster> run(int maxIterations) {
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 每次迭代开始移除所有的数据点
            for (Cluster cluster : clusters) {
                cluster.points.clear();
            }
            assignClusters();
            List<DataPoint> oldCentroids = new ArrayList<>(centroids());
            generateCentroids();
            // 检查形心是否发生变化
            if (listEqual(oldCentroids, centroids())) {
                System.out.println("Converged after " + iteration + " iterations.");
                return clusters;
            }
        }
        return clusters;
    }

    public static void main(String[] args) {
        DataPoint point1 = new DataPoint(List.of(2.0, 1.0, 1.0));
        DataPoint point2 = new DataPoint(List.of(2.0, 2.0, 5.0));
        DataPoint point3 = new DataPoint(List.of(3.0, 1.5, 2.5));

        KMeans<DataPoint> kmeansTest = new KMeans<>(2, List.of(point1, point2, point3));
        List<KMeans<DataPoint>.Cluster> testClusters = kmeansTest.run(100);
        for (int clusterIndex = 0; clusterIndex < testClusters.size(); clusterIndex++) {
            System.out.println("Cluster " + clusterIndex + ": " + testClusters.get(clusterIndex).points);
        }
    }

}
