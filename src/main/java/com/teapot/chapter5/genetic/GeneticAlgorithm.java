package com.teapot.chapter5.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * P95~100
 * 遗传算法
 * 1. 创建随机染色体的初始种群，作为算法的第一代数据
 * 2. 测量这一代种群中每条染色体的适应度。如果有超过阈值的就将其返回，算法结束
 * 3. 选择一些个体进行繁殖，适应度最高的个体被选中的概率更大
 * 4. 某些被选中的染色体以一定的概率进行交换（结合），产生代表下一代种群的后代
 * 5. 通常某些染色体发生突变的概率比较小。新一代的种群已经创建完成，并取代了上一代的种群
 * 6. 返回步骤2继续执行，直到代的数量达到最大值，然后返回当前找到的最优染色体
 */
public class GeneticAlgorithm<C extends Chromosome<C>> {
    /**
     * 选择方法：轮盘式选择法、锦标赛选择法
     */
    public enum SelectionType {
        ROULETTE, TOURNAMENT
    }

    private ArrayList<C> population;
    // 每一代中染色体突变的概率
    private final double mutationChance;
    // 被选中繁殖的双亲生育出带有它们的混合基因的后代的概率
    private final double crossoverChance;
    // 选择方法的类型
    private final SelectionType selectionType;
    private final Random random;

    public GeneticAlgorithm(List<C> initialPopulation, double mutationChance, double crossoverChance, SelectionType selectionType) {
        this.population = new ArrayList<>(initialPopulation);
        this.mutationChance = mutationChance;
        this.crossoverChance = crossoverChance;
        this.selectionType = selectionType;
        this.random = new Random();
    }

    /**
     * 轮盘式选择法：基于每条染色体的适应度占同一代中所有适应度之和的比例进行选择，适应度最高的染色体被选中的概率会更大。
     *
     * @param wheel    每条染色体的适应度
     * @param numPicks
     * @return
     */
    private List<C> pickRoulette(double[] wheel, int numPicks) {
        List<C> picks = new ArrayList<>();
        for (int i = 0; i < numPicks; i++) {
            // 设置0~1的随机值
            double pick = random.nextDouble();
            for (int j = 0; j < wheel.length; j++) {
                // 选择大于阈值的染色体加入其中
                pick -= wheel[j];
                if (pick <= 0) {
                    picks.add(population.get(i));
                    break;
                }
            }
        }
        return picks;
    }

    /**
     * 锦标赛选择法
     *
     * @param numParticipants
     * @param numPicks
     * @return
     */
    private List<C> pickTournament(int numParticipants, int numPicks) {
        Collections.shuffle(population);
        // 第一次挑选
        List<C> tournament = population.subList(0, numParticipants);
        // 对染色体进行排序
        Collections.sort(tournament, Collections.reverseOrder());
        // 选出适应度最佳的染色体
        return tournament.subList(0, numPicks);
    }

    /**
     * 实现繁殖，确保用包含等量染色体的新种群替换上一代的染色体
     */
    private void reproduceAndReplace() {
        ArrayList<C> nextPopulation = new ArrayList<>();
        while (nextPopulation.size() < population.size()) {
            List<C> parents;
            if (selectionType == SelectionType.ROULETTE) {
                double totalFitness = population.stream().mapToDouble(C::fitness).sum();
                double[] wheel = population.stream().mapToDouble(C -> C.fitness() / totalFitness).toArray();
                parents = pickRoulette(wheel, 2);
            } else {
                parents = pickTournament(population.size() / 2, 2);
            }
            // 双亲染色体以一定概率结合，并产生两条新的染色体
            if (random.nextDouble() < crossoverChance) {
                C parent1 = parents.get(0);
                C parent2 = parents.get(1);
                nextPopulation.addAll(parent1.crossover(parent2));
            } else {
                nextPopulation.addAll(parents);
            }
        }

        if (nextPopulation.size() > population.size()) {
            nextPopulation.remove(0);
        }

        population = nextPopulation;
    }

    /**
     * 突变
     */
    private void mutate() {
        for (C individual : population) {
            if (random.nextDouble() < mutationChance) {
                individual.mutate();
            }
        }
    }

    public C run(int maxGenerations, double threshold) {
        C best = Collections.max(population).copy();
        for (int generation = 0; generation < maxGenerations; generation++) {
            if (best.fitness() >= threshold) {
                return best;
            }

            System.out.println("Generation " + generation +
                    " Best " + best.fitness() +
                    " Avg " + population.stream().mapToDouble(C::fitness).average().orElse(0.0));
            reproduceAndReplace();
            mutate();
            C highest = Collections.max(population);
            if (highest.fitness() > best.fitness()) {
                best = highest.copy();
            }
        }
        return best;
    }

}
