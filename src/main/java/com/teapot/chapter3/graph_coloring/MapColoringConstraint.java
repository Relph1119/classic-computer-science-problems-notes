package com.teapot.chapter3.graph_coloring;

import com.teapot.chapter3.csp.CSP;
import com.teapot.chapter3.csp.Constraint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * P56~57
 * 澳大利亚地图着色问题
 * 解决思路：
 * 1. 检查两个地区是否已经赋值，如果其中一个地区额米有着色，不会发生冲突
 * 2. 检查两个地区是否被赋予同样的颜色，如果颜色相同，存在冲突
 */
public class MapColoringConstraint extends Constraint<String, String> {
    private final String place1;
    private final String place2;

    public MapColoringConstraint(String place1, String place2) {
        super(List.of(place1, place2));
        this.place1 = place1;
        this.place2 = place2;
    }

    @Override
    public boolean satisfied(Map<String, String> assignment) {
        if (!assignment.containsKey(place1) || !assignment.containsKey(place2)) {
            return true;
        }

        return !assignment.get(place1).equals(assignment.get(place2));
    }

    public static void main(String[] args) {
        List<String> variables = List.of("Western Australia", "Northern Territory",
                "South Australia", "Queensland", "New South Wales", "Victoria", "Tasmania");
        Map<String, List<String>> domains = new HashMap<>();
        for (String variable : variables) {
            domains.put(variable, List.of("red", "green", "blue"));
        }
        CSP<String, String> csp = new CSP<>(variables, domains);
        csp.addConstraint(new MapColoringConstraint("Western Australia", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Western Australia", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("South Australia", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "New South Wales"));
        csp.addConstraint(new MapColoringConstraint("New South Wales", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "New South Wales"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "Tasmania"));
        Map<String, String> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            System.out.println(solution);
        }
    }
}
