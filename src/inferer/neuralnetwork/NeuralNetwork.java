package inferer.neuralnetwork;

/**
 * Created by Sergiusz on 10.07.2017.
 */
public class NeuralNetwork {
    private int inputs;
    private int layers;
    private double[][][] body;



    public NeuralNetwork(int inputs, int layers, int[] topology) {
        this.inputs = inputs;
        this.layers = layers;
        body = new double[layers][][];

        for (int i = 0; i < layers; i++) {
            int weights = i == 0 ? inputs : topology[i - 1];
            body[i] = new double[topology[i]][weights];
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
        return sum;
    }

    private double activate(double input) {
        return input;
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
    /*private double[][] neuronMatrix;
    private double trainingFactor;

    public NeuralNetwork(int in, int out) {
        inputs = in;
        outputs = out;
        neuronMatrix = new double[outputs][inputs];
        trainingFactor = 0.1;

        for(int i = 0; i < outputs; i++) {
            for (int j = 0; j < inputs; j++) {
                neuronMatrix[i][j] = Math.random();
            }
        }
    }

    public double[] response(double[] inputVector) {
        double[] response = new double[outputs];

        for (int i = 0; i < outputs; i++) {
            double y = 0;
            for (int j = 0; j < inputs; j++) {
                y += inputVector[j] * neuronMatrix[i][j];
            }
            response[i] = y;
        }
        return response;
    }

    public int classify(double[] response) {
        double max = Double.NEGATIVE_INFINITY;
        int result = -1;

        for (int i = 0; i < response.length; i++) {
            if (response[i] > max) {
                max = response[i];
                result = i;
            }
        }
        return result;
    }

    public void trainExample(double[] inputVector, int expectedClassification) {
        double[] response = response(inputVector);
        int classification = classify(response);

        if (classification != expectedClassification) {
            double[] expectedResponse = new double[response.length];

            for (int i = 0; i < expectedResponse.length; i++) {
                expectedResponse[i] = i == expectedClassification ? 1 : 0;
            }

            for (int i = 0; i < outputs; i++) {
                double error = 0;
                for (int j = 0; j < inputs; j++) {
                   // error += 0.5 * () * ();
                }
            }
        }
    }*/
}
