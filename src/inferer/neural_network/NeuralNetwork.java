package inferer.neural_network;

/**
 * Created by Sergiusz on 10.07.2017.
 */
public class NeuralNetwork {
    private int inputs;
    private int layers;
    private double[][][] body;
    private double[] cachedInput;
    private double[][] cachedResponses;
    private double[][] errors;
    private final double alfa;    //alfa parameter of sigmoidal activation function
    private final double biasValue;
    private final double learningRate;


    public NeuralNetwork(int inputs, int layers, int[] topology) {
        this(inputs, layers, topology, 1.0, 1.0, 0.2);
    }

    public NeuralNetwork(int inputs, int layers, int[] topology, double alfa, double biasValue, double learningRate) {
        this.inputs = inputs;
        this.layers = layers;
        this.alfa = alfa;
        this.biasValue = biasValue;
        this.learningRate = learningRate;
        body = new double[layers][][];
        errors = new double[layers][];
        cachedInput = new double[inputs];
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

        cachedInput = input;

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

    private void updateWeights() {
        for (int i = 0; i < body.length; i++) {
            double[] input = i == 0 ? cachedInput : cachedResponses[i - 1];
            for (int j = 0; j < body[i].length; j++) {
                for (int k = 0; k < body[i][j].length - 1; k++) {
                    body[i][j][k] = body[i][j][k] + learningRate * errors[i][j] * input[j];
                }
                body[i][j][body[i][j].length - 1] = body[i][j][body[i][j].length - 1] + learningRate * errors[i][j] * biasValue;
            }
        }
    }

    public double[][] getErrors() {
        return errors;
    }
}
