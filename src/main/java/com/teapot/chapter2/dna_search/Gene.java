package com.teapot.chapter2.dna_search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * P19
 * DNA搜索：线性搜索、二分搜索
 */
public class Gene {
    // 核苷酸
    public enum Nucleotide {
        A, C, G, T
    }

    /**
     * 定义密码子，密码子由三个核苷酸组成
     */
    public static class Codon implements Comparable<Codon> {
        public final Nucleotide first, second, third;
        // 陆续比较三个核苷酸
        private final Comparator<Codon> comparator = Comparator.comparing((Codon c) -> c.first)
                .thenComparing((Codon c) -> c.second)
                .thenComparing((Codon c) -> c.third);

        public Codon(String codonStr) {
            first = Nucleotide.valueOf(codonStr.substring(0, 1));
            second = Nucleotide.valueOf(codonStr.substring(1, 2));
            third = Nucleotide.valueOf(codonStr.substring(2, 3));
        }

        @Override
        public int compareTo(Codon codon) {
            return comparator.compare(this, codon);
        }
    }

    private final ArrayList<Codon> codons = new ArrayList<>();

    /**
     * 每次使用三个支付实例化一个Codon类，添加到codons数组中
     * @param geneStr
     */
    public Gene(String geneStr) {
        for (int i = 0; i < geneStr.length() - 3; i += 3) {
            codons.add(new Codon(geneStr.substring(i, i + 3)));
        }
    }

    /**
     * 使用线性搜索，复杂度为O(n)
     * @param key
     * @return
     */
    public boolean linearContains(Codon key) {
        for (Codon codon : codons) {
            if (codon.compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 二分搜索，复杂度为O(lg n)
     * @param key
     * @return
     */
    public boolean binaryContains(Codon key) {
        ArrayList<Codon> sortedCodons = new ArrayList<>(codons);
        Collections.sort(sortedCodons);
        int low = 0;
        int high = sortedCodons.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            int comparison = codons.get(middle).compareTo(key);
            if (comparison < 0) {
                low = middle + 1;
            } else if (comparison > 0) {
                high = middle - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
        Gene myGene = new Gene(geneStr);
        Codon acg = new Codon("ACG");
        Codon gat = new Codon("GAT");
        System.out.println(myGene.linearContains(acg)); // true
        System.out.println(myGene.linearContains(gat)); // false
        System.out.println(myGene.binaryContains(acg)); // true
        System.out.println(myGene.binaryContains(gat)); // false
    }
}

