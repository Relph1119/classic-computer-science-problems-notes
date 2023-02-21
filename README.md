# 《算法精粹-经典计算机科学问题Java实现》学习笔记

本书主要使用Java语言，讲述了如何编程解决经典的算法问题，首先通过几个小的问题介绍递归、迭代、备忘录模式等解法，之后开始介绍经典算法，包括搜索问题（DFS、BFS、二分搜索、A*搜索）、建立带约束的模板求解问题（回溯思想、八皇后问题、着色问题、字谜问题）、图算法、遗传算法、k均值聚类算法、神经网络模型、极小化极大搜索算法（井字棋、四子棋）、其他问题（背包问题、旅行商问题、笛卡尔积）。

## 环境安装
- Java版本：11.0.10
- Maven版本：3.6.3

## 学习总结
1. 这本书通过Java语言编程求解代码，大量用了`stream`、泛型编程，其中还使用了访问者模式、备忘录模式等经典设计模式
2. 书中的算法求解分析是逐步式的，通过经典问题，构建算法求解模板
3. 应用了很多Java8的新特性，包括`stream`和`lambda`编程，还包括`Optional`、`Predicate`、`Function`、`DoubleSummaryStatistics`类方法；
4. 本书包含各种有趣的示例，比如走迷宫、传教士和食人族问题、井字棋和四子棋AI的构建。
5. 当然，本书的重点是各种算法，包括
   - 搜索算法：DFS、BFS、二分搜索、A*搜索
   - 约束满足问题的通解：CSP求解模板、回溯思想
   - 图问题：构建最小生成树（Jarnik算法）、Dijkstra算法
   - 遗传算法
   - 机器学习和深度学习算法：KMeans算法、神经网络与梯度下降
   - 极小化极大算法
   - 动态规划（DP算法）、笛卡尔积构建