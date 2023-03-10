package com.teapot.chapter9.tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * P187~190
 * 旅行商问题
 * 问题描述：
 * 旅行商必须访问地图上所有的城市，并且每个城市只能被访问一次。旅行社旅途的终点必须是他开始旅行的那个城市。
 * 每个城市都与其他城市之间有道路连接，旅行商可以按照任意顺序来访问这些城市，求最短路径。
 */
public class TSP {
    private final Map<String, Map<String, Integer>> distances;

    public TSP(Map<String, Map<String, Integer>> distances) {
        this.distances = distances;
    }

    public static <T> void swap(T[] array, int first, int second) {
        T temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    /**
     * 使用回溯法，查找列表内数据线的所有排列方案
     */
    private static <T> void allPermutationsHelper(T[] permutation, List<T[]> permutations, int n) {
        if (n <= 0) {
            permutations.add(permutation);
            return;
        }

        T[] tempPermutation = Arrays.copyOf(permutation, permutation.length);
        for (int i = 0; i < n; i++) {
            swap(tempPermutation, i, n - 1);
            allPermutationsHelper(tempPermutation, permutations, n - 1);
            // 回溯
            swap(tempPermutation, i, n - 1);
        }
    }

    private static <T> List<T[]> permutations(T[] original) {
        List<T[]> permutations = new ArrayList<>();
        allPermutationsHelper(original, permutations, original.length);
        return permutations;
    }

    public int pathDistance(String[] path) {
        String last = path[0];
        int distance = 0;
        for (String next : Arrays.copyOfRange(path, 1, path.length)) {
            distance += distances.get(last).get(next);
            last = next;
        }
        return distance;
    }

    /**
     * 使用暴力搜索查看路径列表中的每一条路径，并用两个城市间距离表计算出每条路径的距离
     */
    public String[] findShortestPath() {
        String[] cities = distances.keySet().toArray(String[]::new);
        List<String[]> paths = permutations(cities);
        String[] shortestPath = null;
        int minDistance = Integer.MAX_VALUE;
        for (String[] path : paths) {
            int distance = pathDistance(path);
            distance += distances.get(path[path.length - 1]).get(path[0]);
            if (distance < minDistance) {
                minDistance = distance;
                shortestPath = path;
            }
        }

        shortestPath = Arrays.copyOf(shortestPath, shortestPath.length + 1);
        // 加上旅行商访问的最后一个城市到最开始城市的距离
        shortestPath[shortestPath.length - 1] = shortestPath[0];
        return shortestPath;
    }

    public static void main(String[] args) {
        Map<String, Map<String, Integer>> vtDistances = Map.of(
                "Rutland", Map.of(
                        "Burlington", 67,
                        "White River Junction", 46,
                        "Bennington", 55,
                        "Brattleboro", 75),
                "Burlington", Map.of(
                        "Rutland", 67,
                        "White River Junction", 91,
                        "Bennington", 122,
                        "Brattleboro", 153),
                "White River Junction", Map.of(
                        "Rutland", 46,
                        "Burlington", 91,
                        "Bennington", 98,
                        "Brattleboro", 65),
                "Bennington", Map.of(
                        "Rutland", 55,
                        "Burlington", 122,
                        "White River Junction", 98,
                        "Brattleboro", 40),
                "Brattleboro", Map.of(
                        "Rutland", 75,
                        "Burlington", 153,
                        "White River Junction", 65,
                        "Bennington", 40));
        TSP tsp = new TSP(vtDistances);
        String[] shortestPath = tsp.findShortestPath();
        int distance = tsp.pathDistance(shortestPath);
        System.out.println("The shortest path is " + Arrays.toString(shortestPath) + " in " +
                distance + " miles.");
    }
}
