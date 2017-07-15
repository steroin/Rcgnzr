package inferer.neural_network;

/**
 * Created by Sergiusz on 10.07.2017.
 */
public class NeuralNetwork {
    private int inputs;
    private int layers;
    private double[][][] body;
    private double[][] cachedResponses;
    private double[][] errors;
    private final double alfa = 0.1;    //alfa parameter of sigmoidal activation function
    private final double biasValue = 1.0;


    public NeuralNetwork(int inputs, int layers, int[] topology) {
        this.inputs = inputs;
        this.layers = layers;
        body = new double[layers][][];
        errors = new double[layers][];
        cachedResponses = new double[layers][];

        for (int i = 0; i < layers; i++) {
            int weights = i == 0 ? inputs : topology[i - 1];
            body[i] = new double[topology[i]][weights + 1];
            errors[i] = new double[topology[i]];
            cachedResponses[i] = new double[topology[i]];
        }

    }

    public void setUpWeights(double[][][] weights) {
        body = weights;
    }

    public void randomize() {
        for (int i = 0; i < body.length; i++) {
            for (int j = 0; j < body[i].length; j++) {
                for (int k = 0; k < body[i][j].length; k++) {
                    body[i][j][k] = Math.random();
                }
            }
        }
    }

    private double sum(int layer, int neuron, double[] input) {
        double sum = 0;

        for (int i = 0; i < input.length; i++) {
            sum += input[i] * body[layer][neuron][i];
        }
        int biasIndex = body[layer][neuron].length-1;
        sum += biasValue * body[layer][neuron][biasIndex];
        return sum;
    }

    private double activate(double input) {
        return 1 / (1 + Math.pow(Math.E, -alfa * input));
    }

    public double[] respond(double[] input) {
        int outputs;
        double[] localResponse = null;
        double currentResponse;

        for (int i = 0; i < layers; i++) {
            outputs = body[i].length;
            localResponse = new double[outputs];

            for (int j = 0; j < outputs; j++) {
                currentResponse = activate(sum(i, j, input));
                localResponse[j] = currentResponse;
                cachedResponses[i][j] = currentResponse;
            }
            input = localResponse;
        }
        return localResponse;
    }

    public void calculateErrors(double[] output, double[] expectedOutput) {
        for (int i = 0; i < output.length; i++) {
            errors[layers - 1][i] = (expectedOutput[i] - output[i]) * output[i] * (1 - output[i]);
        }

        for (int i = layers - 2; i >= 0; i--) {              //for every layer backwards, starting from the layer before the last one
            for (int j = 0; j < body[i].length; j++) {      //for every neuron in layer
                double error = 0;

                for (int k = 0; k < errors[i + 1].length; k++) {    //for every error got from next layer
                    error += errors[i + 1][k] * body[i + 1][k][j] * cachedResponses[i][j] * (1 - cachedResponses[i][j]);
                }
                errors[i][j] = error;
            }
        }
    }
    public void train(double[] input, double expectedResponse) {

    }

    public double[][] getErrors() {
        return errors;
    }
}
