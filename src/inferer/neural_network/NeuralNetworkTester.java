package inferer.neural_network;

import data_builder.DataSet;

import java.util.List;

/**
 * Created by Sergiusz on 31.07.2017.
 */
public class NeuralNetworkTester {
    private NeuralNetwork neuralNetwork;
    private DataSet testData;
    private long timeElapsed;

    public NeuralNetworkTester(NeuralNetwork neuralNetwork, DataSet testData) {
        this.neuralNetwork = neuralNetwork;
        this.testData = testData;
    }

    public double getSuccessRate() {
        timeElapsed = System.currentTimeMillis();
        List<double[]> features = testData.getFeatures();
        List<Double> classes = testData.getClassifications();
        int positive = 0;

        for (int i = 0; i < features.size(); i++) {
            int response = neuralNetwork.getKeenResponse(features.get(i));
            if (response == classes.get(i)) {
                positive++;
            }
        }
        timeElapsed = System.currentTimeMillis() - timeElapsed;
        return (double) positive / features.size();
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}
