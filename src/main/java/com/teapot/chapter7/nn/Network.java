package com.teapot.chapter7.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 * P141
 * 神经网络的实现
 */
public class Network<T> {
    private final List<Layer> layers = new ArrayList<>();

    public Network(int[] layerStructure, double learningRate,
                   DoubleUnaryOperator activationFunction, DoubleUnaryOperator derivativeActivationFunction) {
        if (layerStructure.length < 3) {
            throw new IllegalArgumentException("Error: Should be at least 3 layer (1 input, 1 hidden, 1 output)");
        }

        // 输入层
        Layer inputLayer = new Layer(Optional.empty(), layerStructure[0], learningRate, activationFunction, derivativeActivationFunction);
        layers.add(inputLayer);

        // 隐藏层
        for (int i = 1; i < layerStructure.length; i++) {
            Layer nextLayer = new Layer(Optional.of(layers.get(i - 1)), layerStructure[i], learningRate, activationFunction, derivativeActivationFunction);
            layers.add(nextLayer);
        }
    }

    private double[] outputs(double[] inputs) {
        double[] result = inputs;
        for (Layer layer : layers) {
            result = layer.outputs(result);
        }
        return result;
    }

    /**
     * 计算权重
     */
    private void backpropagate(double[] expected) {
        int lastLayer = layers.size() - 1;
        layers.get(lastLayer).calculateDeltasForOutputLayer(expected);
        for (int i = lastLayer - 1; i >= 0; i--) {
            layers.get(i).calculateDetasForHidenLayer(layers.get(i + 1));
        }
    }

    /**
     * 更新权重
     */
    private void updateWeights() {
        for (Layer layer : layers.subList(1, layers.size())) {
            for (Neuron neuron : layer.neurons) {
                for (int w = 0; w < neuron.weights.length; w++) {
                    neuron.weights[w] += neuron.learningRate * layer.previousLayer.get().outputCache[w] * neuron.delta;
                }
            }
        }
    }

    /**
     * 模型训练
     */
    public void train(List<double[]> inputs, List<double[]> expecteds) {
        for (int i = 0; i < inputs.size(); i++) {
            double[] xs = inputs.get(i);
            double[] ys = expecteds.get(i);
            // 每轮训练结束时，对神经元的权重进行修改
            outputs(xs);
            backpropagate(ys);
            updateWeights();
        }
    }

    public class Results {
        public final int correct;
        public final int trials;
        public final double percentage;

        public Results(int correct, int trials, double percentage) {
            this.correct = correct;
            this.trials = trials;
            this.percentage = percentage;
        }
    }

    /**
     * 模型验证
     */
    public Results validate(List<double[]> inputs, List<T> expecteds, Function<double[], T> interpret) {
        int correct = 0;
        for (int i = 0; i < inputs.size(); i++) {
            double[] input = inputs.get(i);
            T expected = expecteds.get(i);
            T result = interpret.apply(outputs(input));
            if ((result.equals(expected))) {
                correct++;
            }
        }
        double percentage = (double) correct / (double) inputs.size();
        return new Results(correct, inputs.size(), percentage);
    }
}
