package com.teapot.chapter5.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * P101
 * 遗传算法简单测试
 */
public class SimpleEquation extends Chromosome<SimpleEquation> {
    private int x, y;

    private static final int MAX_START = 100;

    public SimpleEquation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static SimpleEquation randomInstance() {
        Random random = new Random();
        return new SimpleEquation(random.nextInt(MAX_START), random.nextInt(MAX_START));
    }

    @Override
    public double fitness() {
        return 6 * x - x * x + 4 * y - y * y;
    }

    @Override
    public List<SimpleEquation> crossover(SimpleEquation other) {
        SimpleEquation child1 = new SimpleEquation(x, other.y);
        SimpleEquation child2 = new SimpleEquation(other.x, y);
        return List.of(child1, child2);
    }

    @Override
    public void mutate() {
        Random random = new Random();
        if (random.nextDouble() > 0.5) {
            if (random.nextDouble() > 0.5) {
                x += 1;
            } else {
                x -= 1;
            }
        } else {
            if (random.nextDouble() > 0.5) {
                y += 1;
            } else {
                y -= 1;
            }
        }
    }

    @Override
    public SimpleEquation copy() {
        return new SimpleEquation(x, y);
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Fitness: " + fitness();
    }

    public static void main(String[] args) {
        ArrayList<SimpleEquation> initialPopulation = new ArrayList<>();
        final int POPULATION_SIZE = 20;
        final int GENERATIONS = 100;
        final double THRESHOLD = 13.0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            initialPopulation.add(SimpleEquation.randomInstance());
        }
        GeneticAlgorithm<SimpleEquation> ga = new GeneticAlgorithm<>(
                initialPopulation, 0.1, 0.7, GeneticAlgorithm.SelectionType.TOURNAMENT);
        SimpleEquation result = ga.run(GENERATIONS, THRESHOLD);
        System.out.println(result);
    }
}
