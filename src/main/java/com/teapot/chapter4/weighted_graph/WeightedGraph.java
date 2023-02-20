package com.teapot.chapter4.weighted_graph;

import com.teapot.chapter4.graph.Graph;

import java.util.*;
import java.util.function.IntConsumer;

/**
 * P80
 * 加权图
 * @param <V>
 */
public class WeightedGraph<V> extends Graph<V, WeightedEdge> {
    public WeightedGraph(List<V> vertices) {
        super(vertices);
    }

    public void addEdge(WeightedEdge edge) {
        edges.get(edge.u).add(edge);
        edges.get(edge.v).add(edge.reversed());
    }

    public void addEdge(int u, int v, float weight) {
        addEdge(new WeightedEdge(u, v, weight));
    }

    public void addEdge(V first, V second, float weight) {
        addEdge(indexOf(first), indexOf(second), weight);
    }

    /**
     * 累加列表中所有边的权重
     * @param path
     * @return
     */
    public static double totalWeight(List<WeightedEdge> path) {
        return path.stream().mapToDouble(we -> we.weight).sum();
    }

    /**
     * 生成最小生成树，使用Jarnik算法
     * 1. 选择将要放置于最小生成树中的任一顶点
     * 2. 找到连通最小生成树与尚未加入树的顶点的权重最小的边
     * 3. 将权重最小的边的末端顶点添加到最小生成树中
     * 4 重复第2、3步，直到图中所有顶点都放置于最小生成树中
     * @param start
     * @return
     */
    public List<WeightedEdge> mst(int start) {
        LinkedList<WeightedEdge> result = new LinkedList<>();
        if (start < 0 || start > (getVertexCount() - 1)) {
            return result;
        }
        // 存储新发现的边
        PriorityQueue<WeightedEdge> pq = new PriorityQueue<>();
        // 记录已经访问过的顶点索引
        boolean[] visited = new boolean[getVertexCount()];

        // 将一个顶点标记为已访问，并将尚未访问过的顶点加入到pq中
        IntConsumer visit = index -> {
            visited[index] = true;
            for (WeightedEdge edge: edgesOf(index)) {
                if (!visited[edge.v]) {
                    pq.offer(edge);
                }
            }
        };

        // 提供要开始访问的顶点
        visit.accept(start);
        while (!pq.isEmpty()) {
            // 弹出队列
            WeightedEdge edge = pq.poll();
            // 检查是否为尚未加入树的顶点
            if (visited[edge.v]) {
                continue;
            }
            result.add(edge);
            visit.accept(edge.v);
        }
        return result;
    }

    public void printWeightedPath(List<WeightedEdge> wp) {
        for (WeightedEdge edge: wp) {
            System.out.println(vertexAt(edge.u) + " "
            + edge.weight + "> " + vertexAt(edge.v));
        }
        System.out.println("Total Weight: " + totalWeight(wp));
    }

    public static final class DijkstraNode implements Comparable<DijkstraNode> {
        public final int vertex;
        public final double distance;

        public DijkstraNode(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            Double mine = distance;
            Double theirs = other.distance;
            return mine.compareTo(theirs);
        }
    }

    public static final class DijkstraResult {
        public final double[] distances;
        public final Map<Integer, WeightedEdge> pathMap;

        public DijkstraResult(double[] distances, Map<Integer, WeightedEdge> pathMap) {
            this.distances = distances;
            this.pathMap = pathMap;
        }
    }

    /**
     * Dijkstra算法
     * 1. 将起始顶点加入优先队列
     * 2. 从优先队列中弹出距离最近的顶点，称为当前顶点
     * 3. 逐一查看连接到当前顶点的所有相邻顶点，如果这些顶点尚未被记录过或者到达这些顶点的边是新的最短路径，
     * 就逐个记录它们与起始顶点之间的距离以及产生该距离的边，并把新顶点加入优先队列
     * 4. 重复第2、3步，直到优先队列为空
     * 5. 返回起始顶点到其他所有顶点的最短距离和路径
     * @param root
     * @return
     */
    public DijkstraResult dijkstra(V root) {
        int first = indexOf(root);
        double[] distances = new double[getVertexCount()];
        distances[first] = 0;
        boolean[] visited = new boolean[getVertexCount()];
        visited[first] = true;

        HashMap<Integer, WeightedEdge> pathMap = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        // 第一个放入优先队列的顶点包括顶节点
        pq.offer(new DijkstraNode(first, 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().vertex;
            // 已记录下来的沿着已知路径到达u的距离
            double distU = distances[u];
            // 对连接到u的每条边进行遍历
            for (WeightedEdge we: edgesOf(u)) {
                // 从u到任意已知与之相连的顶点的距离
                double distV = distances[we.v];
                // 正在探索新路径的距离
                double pathWeight = we.weight + distU;
                // 找到了一个尚未被探索的顶点或一条新的最短路径
                if (!visited[we.v] || (distV > pathWeight)) {
                    // 设置已被访问
                    visited[we.v] = true;
                    // 记录到达v的新的最短距离
                    distances[we.v] = pathWeight;
                    // 到达那里的边
                    pathMap.put(we.v, we);
                    // 把新发现的路径所到达的顶点全部放入优先队列中
                    pq.offer(new DijkstraNode(we.v, pathWeight));
                }
            }
        }
        // 返回从根顶点到加权图中每个顶点的距离，以及能够到达这些顶点的最短距离
        return new DijkstraResult(distances, pathMap);
    }

    public Map<V, Double> distanceArrayToDistanceMap(double[] distances) {
        HashMap<V, Double> distanceMap = new HashMap<>();
        for (int i = 0; i < distances.length; i++) {
            distanceMap.put(vertexAt(i), distances[i]);
        }
        return distanceMap;
    }

    public static List<WeightedEdge> pathMapToPath(int start, int end, Map<Integer, WeightedEdge> pathMap) {
        if (pathMap.size() == 0) {
            return List.of();
        }

        LinkedList<WeightedEdge> path = new LinkedList<>();
        WeightedEdge edge = pathMap.get(end);
        path.add(edge);

        while (edge.u != start) {
            edge = pathMap.get(edge.u);
            path.add(edge);
        }
        Collections.reverse(path);
        return path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getVertexCount(); i++) {
            sb.append(vertexAt(i));
            sb.append(" -> ");
            sb.append(Arrays.toString(edgesOf(i).stream()
                    .map(we -> "(" + vertexAt(we.v) + ", " + we.weight + ")").toArray()));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        WeightedGraph<String> cityGraph2 = new WeightedGraph<>(
                List.of("Seattle", "San Francisco", "Los Angeles", "Riverside", "Phoenix", "Chicago", "Boston",
                        "New York", "Atlanta", "Miami", "Dallas", "Houston", "Detroit", "Philadelphia", "Washington"));

        cityGraph2.addEdge("Seattle", "Chicago", 1737);
        cityGraph2.addEdge("Seattle", "San Francisco", 678);
        cityGraph2.addEdge("San Francisco", "Riverside", 386);
        cityGraph2.addEdge("San Francisco", "Los Angeles", 348);
        cityGraph2.addEdge("Los Angeles", "Riverside", 50);
        cityGraph2.addEdge("Los Angeles", "Phoenix", 357);
        cityGraph2.addEdge("Riverside", "Phoenix", 307);
        cityGraph2.addEdge("Riverside", "Chicago", 1704);
        cityGraph2.addEdge("Phoenix", "Dallas", 887);
        cityGraph2.addEdge("Phoenix", "Houston", 1015);
        cityGraph2.addEdge("Dallas", "Chicago", 805);
        cityGraph2.addEdge("Dallas", "Atlanta", 721);
        cityGraph2.addEdge("Dallas", "Houston", 225);
        cityGraph2.addEdge("Houston", "Atlanta", 702);
        cityGraph2.addEdge("Houston", "Miami", 968);
        cityGraph2.addEdge("Atlanta", "Chicago", 588);
        cityGraph2.addEdge("Atlanta", "Washington", 543);
        cityGraph2.addEdge("Atlanta", "Miami", 604);
        cityGraph2.addEdge("Miami", "Washington", 923);
        cityGraph2.addEdge("Chicago", "Detroit", 238);
        cityGraph2.addEdge("Detroit", "Boston", 613);
        cityGraph2.addEdge("Detroit", "Washington", 396);
        cityGraph2.addEdge("Detroit", "New York", 482);
        cityGraph2.addEdge("Boston", "New York", 190);
        cityGraph2.addEdge("New York", "Philadelphia", 81);
        cityGraph2.addEdge("Philadelphia", "Washington", 123);

        System.out.println(cityGraph2);

        List<WeightedEdge> mst = cityGraph2.mst(0);
        cityGraph2.printWeightedPath(mst);

        System.out.println();
        DijkstraResult dijkstraResult = cityGraph2.dijkstra("Los Angeles");
        Map<String, Double> nameDistance = cityGraph2.distanceArrayToDistanceMap(dijkstraResult.distances);
        System.out.println("Distances from Los Angeles:");
        nameDistance.forEach((name, distance) -> System.out.println(name + " : " + distance));

        System.out.println();

        System.out.println("Shortest path from Los Angeles to Boston:");
        List<WeightedEdge> path = pathMapToPath(cityGraph2.indexOf("Los Angeles"), cityGraph2.indexOf("Boston"),
                dijkstraResult.pathMap);
        cityGraph2.printWeightedPath(path);
    }
}
