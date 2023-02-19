package com.teapot.chapter1.compress;

import java.util.BitSet;

/**
 * P8~10
 * 存储基因中的核苷酸序列(A,C,G,T)
 */
public class CompressedGene {
    // 存储核苷酸序列，使用位串形式
    private BitSet bitSet;
    private int length;

    public CompressedGene(String gene) {
        // 使用二进制编码压缩基因
        compress(gene);
    }

    private void compress(String gene) {
        length = gene.length();
        bitSet = new BitSet(length * 2);
        // 将核苷酸序列转换为大写
        final String upperGene = gene.toUpperCase();

        for (int i = 0; i < length; i++) {
            final int firstLocation = 2 * i;
            final int secondLaction = 2 * i + 1;
            switch (upperGene.charAt(i)) {
                case 'A': // 00
                    bitSet.set(firstLocation, false);
                    bitSet.set(secondLaction, false);
                    break;
                case 'C': // 01
                    bitSet.set(firstLocation, false);
                    bitSet.set(secondLaction, true);
                    break;
                case 'G': // 10
                    bitSet.set(firstLocation, true);
                    bitSet.set(secondLaction, false);
                    break;
                case 'T': // 11
                    bitSet.set(firstLocation, true);
                    bitSet.set(secondLaction, true);
                    break;
                default:
                    throw new IllegalArgumentException("The provided gene String contains charaters other than ACGT");
            }
        }
    }

    public String decompress() {
        if (bitSet == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < (length * 2); i += 2) {
            final int firstBit = (bitSet.get(i) ? 1 : 0);
            final int secondBit = (bitSet.get(i + 1) ? 1 : 0);
            final int lastBit = firstBit << 1 | secondBit;
            switch (lastBit) {
                case 0b00:
                    builder.append('A');
                    break;
                case 0b01:
                    builder.append('C');
                    break;
                case 0b10:
                    builder.append('G');
                    break;
                case 0b11:
                    builder.append('T');
                    break;
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        final String original = "TAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATATAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATA";
        CompressedGene compressed = new CompressedGene(original);
        final String decompressed = compressed.decompress();
        System.out.println(decompressed);
        System.out.println("original is the same as decompressed: " +
                original.equalsIgnoreCase(decompressed));
    }
}
