package com.teapot.chapter2.maze;

import com.teapot.chapter2.GenericSearch;
import com.teapot.chapter2.GenericSearch.Node;

import java.util.*;

/**
 * P27~42
 * 迷宫求解
 * 迷宫是由一些Cell组成的二维网格，分别使用DFS、BFS、AStar搜索解决迷宫问题
 */
public class Maze {
    public enum Cell {
        EMPTY(" "),
        BLOCKED("X"),
        START("S"),
        GOAL("G"),
        PATH("*");

        private final String code;

        Cell(String c) {
            code = c;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public static class MazeLocation {
        public final int row;
        public final int column;

        public MazeLocation(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MazeLocation that = (MazeLocation) o;

            if (row != that.row) return false;
            return column == that.column;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }
    }

    private final int rows, columns;
    private final MazeLocation start, goal;
    private final Cell[][] grid;

    /**
     * 随机生成迷宫
     *
     * @param sparseness 稀疏度
     */
    public Maze(int rows, int columns, MazeLocation start, MazeLocation goal, double sparseness) {
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.goal = goal;
        grid = new Cell[rows][columns];
        for (Cell[] row : grid) {
            Arrays.fill(row, Cell.EMPTY);
        }

        randomlyFill(sparseness);
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public Maze() {
        this(10, 10, new MazeLocation(0, 0), new MazeLocation(9, 9), 0.2);
    }

    /**
     * 随机填充迷宫路障
     *
     * @param sparseness 默认为0.2
     */
    private void randomlyFill(double sparseness) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Math.random() < sparseness) {
                    grid[row][column] = Cell.BLOCKED;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                sb.append(cell.toString());
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 判断是否到达终点
     */
    public boolean goalTest(MazeLocation ml) {
        return goal.equals(ml);
    }

    /**
     * 检查当前位置的上下左右是否存在可以行进的路径
     */
    public List<MazeLocation> successors(MazeLocation ml) {
        List<MazeLocation> locations = new ArrayList<>();
        if (ml.row + 1 < rows && grid[ml.row + 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row + 1, ml.column));
        }
        if (ml.row - 1 >= 0 && grid[ml.row - 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row - 1, ml.column));
        }
        if (ml.column + 1 < columns && grid[ml.row][ml.column + 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column + 1));
        }
        if (ml.column - 1 >= 0 && grid[ml.row][ml.column - 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column - 1));
        }
        return locations;
    }

    /**
     * 标记迷宫中的成功路径、起始路径和终点路径
     */
    public void mark(List<MazeLocation> path) {
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.PATH;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    /**
     * 移除已标记的路径
     */
    public void clear(List<MazeLocation> path) {
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.EMPTY;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    /**
     * 计算欧式距离
     */
    public double euclideanDistance(MazeLocation ml) {
        int xdist = ml.column - goal.column;
        int ydist = ml.row - goal.row;
        return Math.sqrt((xdist * xdist) + (ydist * ydist));
    }

    public double manhattanDistance(MazeLocation ml) {
        int xdist = ml.column - goal.column;
        int ydist = ml.row - goal.row;
        return xdist + ydist;
    }

    public static void main(String[] args) {
        Maze m = new Maze();
        System.out.println(m);

        Node<MazeLocation> solution1 = GenericSearch.dfs(m.start, m::goalTest, m::successors);
        if (solution1 == null) {
            System.out.println("No solution found using depth-first search!");
        } else {
            List<MazeLocation> path1 = GenericSearch.nodeToPath(solution1);
            m.mark(path1);
            System.out.println(m);
            m.clear(path1);
        }

        Node<MazeLocation> solution2 = GenericSearch.bfs(m.start, m::goalTest, m::successors);
        if (solution2 == null) {
            System.out.println("No solution found using depth-first search!");
        } else {
            List<MazeLocation> path2 = GenericSearch.nodeToPath(solution2);
            m.mark(path2);
            System.out.println(m);
            m.clear(path2);
        }

        Node<MazeLocation> solution3 = GenericSearch.astar(m.start, m::goalTest, m::successors, m::manhattanDistance);
        if (solution3 == null) {
            System.out.println("No solution found using depth-first search!");
        } else {
            List<MazeLocation> path3 = GenericSearch.nodeToPath(solution3);
            m.mark(path3);
            System.out.println(m);
            m.clear(path3);
        }
    }
}
