package com.teapot.chapter3.csp;

import java.util.List;
import java.util.Map;

/**
 * P51
 * 约束
 * V表示变量，D表示值域
 */
public abstract class Constraint<V, D> {
    protected List<V> variables;

    public Constraint(List<V> variables) {
        this.variables = variables;
    }

    public abstract boolean satisfied(Map<V, D> assignment);
}
