package com.teapot.chapter3.eight_queens;

import com.teapot.chapter3.csp.CSP;
import com.teapot.chapter3.csp.Constraint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 八皇后问题
 */
public class QueensConstraint extends Constraint<Integer, Integer> {
    private List<Integer> columns;

    public QueensConstraint(List<Integer> columns) {
        super(columns);
        this.columns = columns;
    }

    @Override
    public boolean satisfied(Map<Integer, Integer> assignment) {
        for (Map.Entry<Integer, Integer> item : assignment.entrySet()) {
            int q1c = item.getKey();
            int q1r = item.getValue();
            for (int q2c = q1c + 1; q2c <= columns.size(); q2c++) {
                if (assignment.containsKey(q2c)) {
                    int q2r = assignment.get(q2c);
                    if (q1r == q2r) {
                        return false;
                    }
                    if (Math.abs(q1r - q2r) == Math.abs(q1c - q2c)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        List<Integer> columns = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Map<Integer, List<Integer>> rows = new HashMap<>();
        for (int column : columns) {
            rows.put(column, List.of(1, 2, 3, 4, 5, 6, 7, 8));
        }
        CSP<Integer, Integer> csp = new CSP<>(columns, rows);
        csp.addConstraint(new QueensConstraint(columns));
        Map<Integer, Integer> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            System.out.println(solution);
        }
    }
}
