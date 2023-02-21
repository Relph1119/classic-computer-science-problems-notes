package com.teapot.chapter9.knapsack;

import java.util.ArrayList;
import java.util.List;

/**
 * P181
 * 背包问题
 */
public class Knapsack {
    /**
     * 背包里面的物品
     */
    public static final class Item {
        public final String name;
        public final int weight;
        public final double value;

        public Item(String name, int weight, double value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }
    }

    public static List<Item> knapsack(List<Item> items, int maxCapacity) {
        // 构建一个表，包含每种容量和物品数量组合的最优解
        double[][] table = new double[items.size() + 1][maxCapacity + 1];
        for (int i = 0; i < items.size(); i++) {
            // i表示在每个已搜索的容量以内前i个物品的可能组合
            Item item = items.get(i);
            for (int capacity = 1; capacity <= maxCapacity; capacity++) {
                // 正在探索的当前容量以内最后一种物品组合的价值
                double prevItemValue = table[i][capacity];
                if (capacity >= item.weight) {
                    // 从当前容量中减去该物品重量后的容量对应的最后一次物品组合的价值
                    double valueFreeingWeightForItem = table[i][capacity - item.weight];
                    table[i + 1][capacity] = Math.max(valueFreeingWeightForItem + item.value, prevItemValue);
                } else {
                    table[i + 1][capacity] = prevItemValue;
                }
            }
        }

        List<Item> solution = new ArrayList<>();
        int capacity = maxCapacity;
        for (int i = items.size(); i > 0; i--) {
            // 总价值有变化，就把物品加入到结果中
            if (table[i - 1][capacity] != table[i][capacity]) {
                solution.add(items.get(i - 1));
                capacity -= items.get(i - 1).weight;
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("television", 50, 500));
        items.add(new Item("candlesticks", 2, 300));
        items.add(new Item("stereo", 35, 400));
        items.add(new Item("laptop", 3, 1000));
        items.add(new Item("food", 15, 50));
        items.add(new Item("clothing", 20, 800));
        items.add(new Item("jewelry", 1, 4000));
        items.add(new Item("books", 100, 300));
        items.add(new Item("printer", 18, 30));
        items.add(new Item("refrigerator", 200, 700));
        items.add(new Item("painting", 10, 1000));
        List<Item> toSteal = knapsack(items, 75);
        System.out.println("The best items for the thief to steal are:");
        System.out.printf("%-15.15s %-15.15s %-15.15s%n", "Name", "Weight", "Value");
        for (Item item : toSteal) {
            System.out.printf("%-15.15s %-15.15s %-15.15s%n", item.name, item.weight, item.value);
        }
    }
}
