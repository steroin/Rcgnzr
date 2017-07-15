package inferer.neuralnetwork;

/**
 * Created by Sergiusz on 10.07.2017.
 */
public class NeuralNetwork {
    private int inputs;
    private int layers;
    private double[][][] body;
    private final double alfa = 2.0;    //alfa parameter of sigmoidal activation function
    private final double biasValue = 1.0;


    public NeuralNetwork(int inputs, int layers, int[] topology) {
        this.inputs = inputs;
        this.layers = layers;
        body = new double[layers][][];

        for (int i = 0; i < layers; i++) {
            int weights = i == 0 ? inputs : topology[i - 1];
            body[i] = new double[topology[i]][weights + 1];
        }
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

        for (int i = 0; i < layers; i++) {
            outputs = body[i].length;
            localResponse = new double[outputs];

            for (int j = 0; j < outputs; j++) {
                localResponse[j] = activate(sum(i, j, input));
            }
            input = localResponse;
        }
        return localResponse;
    }

    public void train(double[] input, double expectedResponse) {

    }
}
