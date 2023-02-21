package com.teapot.chapter7.iris;

import com.teapot.chapter7.nn.Network;
import com.teapot.chapter7.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * P147
 * 鸢尾花数据集分类任务
 */
public class IrisTest {
    public static final String IRIS_SETOSA = "Iris-setosa";
    public static final String IRIS_VERSICOLOR = "Iris-versicolor";
    public static final String IRIS_VIRGINICA = "Iris-virginica";

    private final List<double[]> irisParameters = new ArrayList<>();
    private final List<double[]> irisClassifications = new ArrayList<>();
    private final List<String> irisSpecies = new ArrayList<>();

    public IrisTest() {
        List<String[]> irisDataset = Util.loadCSV("data/iris.csv");
        // 随机打乱数据
        Collections.shuffle(irisDataset);
        for (String[] iris : irisDataset) {
            double[] parameters = Arrays.stream(iris)
                    .limit(4)
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            irisParameters.add(parameters);
            String species = iris[4];
            switch (species) {
                case IRIS_SETOSA:
                    irisClassifications.add(new double[]{1.0, 0.0, 0.0});
                    break;
                case IRIS_VERSICOLOR:
                    irisClassifications.add(new double[]{0.0, 1.0, 0.0});
                    break;
                default:
                    irisClassifications.add(new double[]{0.0, 0.0, 1.0});
                    break;
            }
            irisSpecies.add(species);
        }
        // 对所有特征进行归一化
        Util.normalizeByFeatureScaling(irisParameters);
    }

    public String irisInterpretOutput(double[] output) {
        double max = Util.max(output);
        if (max == output[0]) {
            return IRIS_SETOSA;
        } else if (max == output[1]) {
            return IRIS_VERSICOLOR;
        } else {
            return IRIS_VIRGINICA;
        }
    }

    public Network<String>.Results classify() {
        // 指定1个输入层、1个隐藏层、1个输出层，其中输入层有4个神经元，隐藏层有6个神经元，输出层有3个神经元
        Network<String> irisNetwork = new Network<>(new int[]{4, 6, 3}, 0.3, Util::sigmoid, Util::derivativeSigmoid);
        // 将前140条数据作为训练集
        List<double[]> irisTrainers = irisParameters.subList(0, 140);
        List<double[]> irisTrainersCorrects = irisClassifications.subList(0, 140);
        // 进行50次训练
        int trainingIterations = 50;
        for (int i = 0; i < trainingIterations; i++) {
            irisNetwork.train(irisTrainers, irisTrainersCorrects);
        }
        // 将最后10条数据作为验证集
        List<double[]> irisTesters = irisParameters.subList(140, 150);
        List<String> irisTestersCorrects = irisSpecies.subList(140, 150);
        return irisNetwork.validate(irisTesters, irisTestersCorrects, this::irisInterpretOutput);
    }

    public static void main(String[] args) {
        IrisTest irisTest = new IrisTest();
        Network<String>.Results results = irisTest.classify();
        System.out.println(results.correct + " correct of " + results.trials + " = " +
                results.percentage * 100 + "%");
    }

}
