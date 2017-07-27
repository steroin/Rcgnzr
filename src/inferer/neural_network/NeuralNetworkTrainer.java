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
    private DataSet validationData;
    private DataSet testData;

    public NeuralNetworkTrainer(NeuralNetwork nn, DataSet training, DataSet validation, DataSet test) {
        neuralNetwork = nn;
        trainingData = training;
        validationData = validation;
        testData = test;
    }

    public void train() {
        List<double[]> inputs = trainingData.getFeatures();
        List<Double> outputs = trainingData.getClassifications();

        neuralNetwork.randomize();

        for (int i = 0; i < inputs.size(); i++) {
            double[] expectedOutput = buildOutputVector(outputs.get(i).intValue());
            double[] output = neuralNetwork.respond(inputs.get(i));
            neuralNetwork.calculateErrors(output, expectedOutput);
            neuralNetwork.updateWeights();
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

    private double getErrorRate(DataSet set) {
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

    public double[] chooseParameters(double[] alfaParams, double[] biasValueParams, double[] learningRateParams) {
        double[] bestParams = new double[3];
        double bestError = Double.POSITIVE_INFINITY;

        for (double alfa : alfaParams) {
            neuralNetwork.setAlfa(alfa);
            for (double biasValue : biasValueParams) {
                neuralNetwork.setBiasValue(biasValue);
                for (double learningRate : learningRateParams) {
                    train();
                    neuralNetwork.setLearningRate(learningRate);
                    double error = getErrorRate(validationData);
                    if (error < bestError) {
                        bestError = error;
                        bestParams[0] = alfa;
                        bestParams[1] = biasValue;
                        bestParams[2] = learningRate;
                    }

                }
            }
        }
        System.out.println("Best params: ["+bestParams[0]+", "+bestParams[1]+", "+bestParams[2]+"] - error: "+bestError);
        return bestParams;
    }
}
