package com.teapot.chapter3.csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * P52
 * 约束满足问题（Constraint Satisfaction Problem, CSP）
 *
 * @param <V> 变量
 * @param <D> 值域
 */
public class CSP<V, D> {
    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints = new HashMap<>();

    public CSP(List<V> variables, Map<V, List<D>> domains) {
        this.variables = variables;
        this.domains = domains;
        for (V variable : variables) {
            constraints.put(variable, new ArrayList<>());
            if (!domains.containsKey(variable)) {
                throw new IllegalArgumentException("Every variable should have a domain assigned to it.");
            }
        }
    }

    public void addConstraint(Constraint<V, D> constraint) {
        for (V variable : constraint.variables) {
            if (!variables.contains(variable)) {
                throw new IllegalArgumentException("Variable in constraint not in CSP");
            }
            constraints.get(variable).add(constraint);
        }
    }

    /**
     * 判断给定的变量配置和所选域值是否满足约束
     *
     * @param variable   变量
     * @param assignment 变量配置
     * @return
     */
    public boolean constraint(V variable, Map<V, D> assignment) {
        // 遍历给定变量的每个约束
        for (Constraint<V, D> constraint : constraints.get(variable)) {
            if (!constraint.satisfied(assignment)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 回溯：在搜索中一旦碰到障碍，就回到碰到障碍之前最后一次做出判断的已知点
     * 回溯搜索是一种递归式深度优先搜索
     *
     * @param assignment 变量配置
     * @return
     */
    public Map<V, D> backtrackingSearch(Map<V, D> assignment) {
        // 找到满足条件的赋值
        if (assignment.size() == variables.size()) {
            return assignment;
        }

        // 选出一个新变量并搜索其值域
        V unassigned = variables.stream().filter(v -> !assignment.containsKey(v)).findFirst().get();
        // 遍历该变量所有的可能分配到的值域
        for (D value : domains.get(unassigned)) {
            Map<V, D> localAssignment = new HashMap<>(assignment);
            localAssignment.put(unassigned, value);
            // 如果约束都满足
            if (constraint(unassigned, localAssignment)) {
                // 继续回溯搜索
                Map<V, D> result = backtrackingSearch(localAssignment);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Map<V, D> backtrackingSearch() {
        return backtrackingSearch(new HashMap<>());
    }
}
