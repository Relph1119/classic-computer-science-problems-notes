package com.teapot.chapter5.genetic;

import java.util.List;

/**
 * P95
 * 染色体
 */
public abstract class Chromosome<T extends Chromosome<T>> implements Comparable<T> {
    /**
     * 适应度
     */
    public abstract double fitness();

    /**
     * 交换，将自身与另一个染色体混合
     */
    public abstract List<T> crossover(T other);

    /**
     * 突变
     */
    public abstract void mutate();

    /**
     * 自我复制
     */
    public abstract T copy();

    @Override
    public int compareTo(T other) {
        Double mine = this.fitness();
        Double theirs = other.fitness();
        return mine.compareTo(theirs);
    }
}
