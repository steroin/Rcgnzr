package inferer.neural_network;

import data_builder.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 15.07.2017.
 */
public class NeuralNetworkTrainer {
    private NeuralNetwork neuralNetwork;
    private DataSet trainingData;
    private int epochs;

    public NeuralNetworkTrainer(NeuralNetwork neuralNetwork, DataSet trainingData, int epochs) {
        this.neuralNetwork = neuralNetwork;
        this.trainingData = trainingData;
        this.epochs = epochs;
    }

    public void train() {
        List<double[]> inputs = trainingData.getFeatures();
        List<Double> outputs = trainingData.getClassifications();

        neuralNetwork.randomize();

        for (int i = 0; i < epochs; i++) {
            for (int j = 0; j < inputs.size(); j++) {
                double[] expectedOutput = buildOutputVector(outputs.get(j).intValue());
                double[] output = neuralNetwork.respond(inputs.get(j));
                neuralNetwork.calculateErrors(output, expectedOutput);
                neuralNetwork.updateWeights();
            }
            trainingData.shuffle();
        }
    }

    private double[] buildOutputVector(int output) {
        double[] outputVector = new double[neuralNetwork.getOutputs()];

        for (int i = 0; i < outputVector.length; i++) {
            if (i == output) outputVector[i] = 1;
            else outputVector[i] = 0;
        }
        return outputVector;
    }

    public double getErrorRate(DataSet set) {
        List<double[]> inputs = set.getFeatures();
        List<Double> expectedOutputs = set.getClassifications();
        int total = 0;
        int missClassify = 0;

        for (int i = 0; i < inputs.size(); i++) {
            double[] output = neuralNetwork.respond(inputs.get(i));
            double expectedOutput = expectedOutputs.get(i);

            if (getMaxVectorIndex(output) != expectedOutput) {
                missClassify++;
            }
            total++;
        }
        return (double) missClassify / total;
    }

    private int getMaxVectorIndex(double[] vector) {
        double max = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;

        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > max) {
                max = vector[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
