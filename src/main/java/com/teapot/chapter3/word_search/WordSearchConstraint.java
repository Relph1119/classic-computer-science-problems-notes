package com.teapot.chapter3.word_search;

import com.teapot.chapter3.csp.CSP;
import com.teapot.chapter3.csp.Constraint;
import com.teapot.chapter3.word_search.WordGrid.GridLocation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * P63~64
 * 单词搜索问题：
 * 单词需要位于网格范围内的行、列或对角线上
 */
public class WordSearchConstraint extends Constraint<String, List<GridLocation>> {

    public WordSearchConstraint(List<String> words) {
        super(words);
    }

    @Override
    public boolean satisfied(Map<String, List<GridLocation>> assignment) {
        List<GridLocation> allLocations = assignment.values().stream()
                .flatMap(Collection::stream).collect(Collectors.toList());

        Set<GridLocation> allLocationsSet = new HashSet<>(allLocations);
        // 检查单词的位置是否相同
        return allLocations.size() == allLocationsSet.size();
    }

    public static void main(String[] args) {
        WordGrid grid = new WordGrid(9, 9);
        List<String> words = List.of("MATTHEW", "JOE", "MARY", "SARAH", "SALLY");
        Map<String, List<List<GridLocation>>> domains = new HashMap<>();
        for (String word : words) {
            domains.put(word, grid.generateDomain(word));
        }
        CSP<String, List<GridLocation>> csp = new CSP<>(words, domains);
        csp.addConstraint(new WordSearchConstraint(words));
        Map<String, List<GridLocation>> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            Random random = new Random();
            for (Map.Entry<String, List<GridLocation>> item : solution.entrySet()) {
                String word = item.getKey();
                List<GridLocation> locations = item.getValue();
                // 随机选取一些单词进行逆序处理
                if (random.nextBoolean()) {
                    Collections.reverse(locations);
                }
                grid.mark(word, locations);
            }
            System.out.println(grid);
        }
    }
}
