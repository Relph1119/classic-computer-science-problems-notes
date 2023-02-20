package com.teapot.chapter2.missionaries_and_cannibals;

import com.teapot.chapter2.GenericSearch;
import com.teapot.chapter2.GenericSearch.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * P44
 * 传教士与食人族问题
 * 问题描述：假设在河的西岸有3名传教士和3名食人族，他们都必须渡过河到东岸，只有一艘可以容纳2人的小船，
 * 在河的两岸，食人族的人数不能多于传教士，否则食人族会吃到传教士。为了渡河，独木舟上至少要有1人
 */
public class MCState {
    private static final int MAX_NUM = 3;
    // 西岸的传教士
    private final int wm;
    // 西岸的食人族
    private final int wc;
    // 东岸的传教士
    private final int em;
    // 东岸的食人族
    private final int ec;
    // 船是否在西岸
    private final boolean boat;

    public MCState(int missionaries, int cannibals, boolean boat) {
        wm = missionaries;
        wc = cannibals;
        em = MAX_NUM - wm;
        ec = MAX_NUM - wc;
        this.boat = boat;
    }

    @Override
    public String toString() {
        return String.format(
                "On the west bank there are %d missionaries and %d cannibals.%n"
                        + "On the east bank there are %d missionaries and %d cannibals.%n"
                        + "The boat is on the %s bank.",
                wm, wc, em, ec,
                boat ? "west" : "east");
    }

    /**
     * 检验当前状态是否为目标状态，即是否完成渡河
     */
    public boolean goalTest() {
        return isLegal() && em == MAX_NUM && ec == MAX_NUM;
    }

    public boolean isLegal() {
        if (wm < wc && wm > 0) {
            return false;
        }
        if (em < ec && em > 0) {
            return false;
        }
        return true;
    }

    public static List<MCState> successors(MCState mcs) {
        List<MCState> sucs = new ArrayList<>();
        // 遍历所有的步骤
        if (mcs.boat) {
            if (mcs.wm > 1) {
                sucs.add(new MCState(mcs.wm - 2, mcs.wc, !mcs.boat));
            }
            if (mcs.wm > 0) {
                sucs.add(new MCState(mcs.wm - 1, mcs.wc, !mcs.boat));
            }
            if (mcs.wc > 1) {
                sucs.add(new MCState(mcs.wm, mcs.wc - 2, !mcs.boat));
            }
            if (mcs.wc > 0) {
                sucs.add(new MCState(mcs.wm, mcs.wc - 1, !mcs.boat));
            }
            if (mcs.wc > 0 && mcs.wm > 0) {
                sucs.add(new MCState(mcs.wm - 1, mcs.wc - 1, !mcs.boat));
            }
        } else {
            if (mcs.em > 1) {
                sucs.add(new MCState(mcs.wm + 2, mcs.wc, !mcs.boat));
            }
            if (mcs.em > 0) {
                sucs.add(new MCState(mcs.wm + 1, mcs.wc, !mcs.boat));
            }
            if (mcs.ec > 1) {
                sucs.add(new MCState(mcs.wm, mcs.wc + 2, !mcs.boat));
            }
            if (mcs.ec > 0) {
                sucs.add(new MCState(mcs.wm, mcs.wc + 1, !mcs.boat));
            }
            if (mcs.ec > 0 && mcs.em > 0) {
                sucs.add(new MCState(mcs.wm + 1, mcs.wc + 1, !mcs.boat));
            }
        }
        // 过滤掉不符合要求的步骤
        sucs.removeIf(Predicate.not(MCState::isLegal));
        return sucs;
    }

    /**
     * 打印求解步骤
     */
    public static void displaySolution(List<MCState> path) {
        if (path.size() == 0) {
            return;
        }
        MCState oldState = path.get(0);
        System.out.println(oldState);
        for (MCState currentState : path.subList(1, path.size())) {
            if (currentState.boat) {
                System.out.printf("%d missionaries and %d cannibals moved from the east bank to the west bank.%n",
                        oldState.em - currentState.em,
                        oldState.ec - currentState.ec);
            } else {
                System.out.printf("%d missionaries and %d cannibals moved from the east bank to the west bank.%n",
                        oldState.wm - currentState.wm,
                        oldState.wc - currentState.wc);
            }
            System.out.println(currentState);
            oldState = currentState;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCState mcState = (MCState) o;

        if (wm != mcState.wm) return false;
        if (wc != mcState.wc) return false;
        if (em != mcState.em) return false;
        if (ec != mcState.ec) return false;
        return boat == mcState.boat;
    }

    @Override
    public int hashCode() {
        int result = wm;
        result = 31 * result + wc;
        result = 31 * result + em;
        result = 31 * result + ec;
        result = 31 * result + (boat ? 1 : 0);
        return result;
    }

    public static void main(String[] args) {
        MCState start = new MCState(MAX_NUM, MAX_NUM, true);
        Node<MCState> solution = GenericSearch.bfs(start, MCState::goalTest, MCState::successors);
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            List<MCState> path = GenericSearch.nodeToPath(solution);
            displaySolution(path);
        }
    }
}
