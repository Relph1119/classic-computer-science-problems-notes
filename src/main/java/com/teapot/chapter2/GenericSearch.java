package com.teapot.chapter2;

import com.teapot.chapter2.maze.Maze;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * P25~26
 * 通用示例
 */
public class GenericSearch {
    public static <T extends Comparable<T>> boolean linearContains(List<T> list, T key) {
        for (T item : list) {
            if (item.compareTo(key) == 0) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static <T extends Comparable<T>> boolean binaryContains(List<T> list, T key) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            int comparison = list.get(middle).compareTo(key);
            if (comparison < 0) {
                low = middle + 1;
            } else if (comparison > 0) {
                high = middle - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public static class Node<T> implements Comparable<Node<T>> {
        final T state;
        Node<T> parent;
        // 成本值
        double cost;
        // 启发值
        double heuristic;

        Node(T state, Node<T> parent) {
            this.state = state;
            this.parent = parent;
        }

        public Node(T state, Node<T> parent, double cost, double heuristic) {
            this.state = state;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node<T> other) {
            Double mine = cost + heuristic;
            Double theirs = other.cost + other.heuristic;
            return mine.compareTo(theirs);
        }
    }

    /**
     * P33
     * 深度优先搜索
     *
     * @param initial
     * @param goalTest   到达终点的谓词条件
     * @param successors
     * @return
     */
    public static <T> Node<T> dfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors) {
        // 记录当前要搜索的状态
        Stack<Node<T>> frontier = new Stack<>();
        frontier.push(new Node<>(initial, null));
        // 记录已经搜索过的状态
        Set<T> explored = new HashSet<>();
        explored.add(initial);

        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.pop();
            T currentState = currentNode.state;
            if (goalTest.test(currentState)) {
                return currentNode;
            }

            for (T child : successors.apply(currentState)) {
                if (explored.contains(child)) {
                    continue;
                }
                // 防止重复访问之前已经访问过的状态
                explored.add(child);
                frontier.push(new Node<>(child, currentNode));
            }
        }
        return null;
    }

    /**
     * P37
     * 广度优先搜索
     *
     * @param initial
     * @param goalTest   到达终点的谓词条件
     * @param successors
     * @return
     */
    public static <T> Node<T> bfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors) {
        // 记录当前要搜索的状态
        Queue<Node<T>> frontier = new LinkedList<>();
        frontier.offer(new Node<>(initial, null));
        // 记录已经搜索过的状态
        Set<T> explored = new HashSet<>();
        explored.add(initial);

        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.poll();
            T currentState = currentNode.state;
            if (goalTest.test(currentState)) {
                return currentNode;
            }

            for (T child : successors.apply(currentState)) {
                if (explored.contains(child)) {
                    continue;
                }
                // 防止重复访问之前已经访问过的状态
                explored.add(child);
                frontier.offer(new Node<>(child, currentNode));
            }
        }
        return null;
    }

    /**
     * P41
     * A*搜索：使用成本函数g(n)和启发函数h(n)，将搜索聚焦在最有可能快速抵达目标的路径上
     * f(n) = g(n) + h(n)
     * A*搜索会选择f(n)最小的那个状态
     * @param initial
     * @param goalTest
     * @param successors
     * @param heuristic
     * @param <T>
     * @return
     */
    public static <T> Node<T> astar(T initial, Predicate<T> goalTest, Function<T, List<T>> successors, ToDoubleFunction<T> heuristic) {
        PriorityQueue<Node<T>> frontier = new PriorityQueue<>();
        frontier.offer(new Node<>(initial, null, 0.0, heuristic.applyAsDouble(initial)));
        Map<T, Double> explored = new HashMap<>();
        explored.put(initial, 0.0);

        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.poll();
            T currentState = currentNode.state;
            if (goalTest.test(currentState)) {
                return currentNode;
            }

            for (T child : successors.apply(currentState)) {
                double newCost = currentNode.cost + 1;
                // f(n)小于之前的那个
                if (!explored.containsKey(child) || explored.get(child) > newCost) {
                    explored.put(child, newCost);
                    frontier.offer(new Node<>(child, currentNode, newCost, heuristic.applyAsDouble(child)));
                }
            }
        }
        return null;
    }

    public static <T> List<T> nodeToPath(Node<T> node) {
        List<T> path = new ArrayList<>();
        path.add(node.state);
        // 得到从起点到目标点的路径
        while (node.parent != null) {
            node = node.parent;
            path.add(0, node.state);
        }
        return path;
    }

    public static void main(String[] args) {
        System.out.println(linearContains(List.of(1, 5, 15, 15, 15, 15, 20), 5)); // true
        System.out.println(binaryContains(List.of("a", "d", "e", "f", "z"), "f")); // true
        System.out.println(binaryContains(List.of("john", "mark", "ronald", "sarah"), "sheila")); // false
    }
}
