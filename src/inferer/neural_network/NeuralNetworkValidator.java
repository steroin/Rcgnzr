package inferer.neural_network;

import data_builder.DataSet;

/**
 * Created by Sergiusz on 31.07.2017.
 */
public class NeuralNetworkValidator {
    private NeuralNetwork neuralNetwork;
    private NeuralNetworkTrainer trainer;
    private DataSet validationData;
    private double[] alfas;
    private double[] biasValues;
    private double[] learningRates;

    public NeuralNetworkValidator(NeuralNetwork neuralNetwork, NeuralNetworkTrainer trainer, DataSet validationData, double[] alfas, double[] biasValues,
                                  double[] learningRates) {
        this.neuralNetwork = neuralNetwork;
        this.validationData = validationData;
        this.alfas = alfas;
        this.biasValues = biasValues;
        this.learningRates = learningRates;
    }

    public void chooseBestParams() {
        double[] bestParams = new double[3];
        double bestError = Double.POSITIVE_INFINITY;

        for (double alfa : alfas) {
            neuralNetwork.setAlfa(alfa);
            for (double biasValue : biasValues) {
                neuralNetwork.setBiasValue(biasValue);
                for (double learningRate : learningRates) {
                    trainer.train();
                    neuralNetwork.setLearningRate(learningRate);
                    double error = trainer.getErrorRate(validationData);
                    if (error < bestError) {
                        bestError = error;
                        bestParams[0] = alfa;
                        bestParams[1] = biasValue;
                        bestParams[2] = learningRate;
                    }

                }
            }
        }
        neuralNetwork.setAlfa(bestParams[0]);
        neuralNetwork.setBiasValue(bestParams[1]);
        neuralNetwork.setLearningRate(bestParams[2]);
    }
}
