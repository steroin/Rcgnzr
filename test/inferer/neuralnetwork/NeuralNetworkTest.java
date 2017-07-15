package inferer.neuralnetwork;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sergiusz on 15.07.2017.
 */
public class NeuralNetworkTest {

    @Test
    public void respond() throws Exception {
        NeuralNetwork nn = new NeuralNetwork(5, 2, new int[]{4, 4});
        double[][][] body = new double[][][]{
                {
                        {0.4, 0.25, 0.45, 0.9, 0.1, 0.1},
                        {0.65, 0.8, 0.5, 0.15, 0.3, 0.2},
                        {0.7, 1, 0.35, 0.2, 0.2, 0.5},
                        {0.1 ,0.3, 0.5, 0.7, 0.4, 0.9}
                },
                {
                        {0.3, 0.1, 0.8, 0.25, 0.8},
                        {0.8, 0.4, 0.75, 0.4, 0.6},
                        {0.7, 0.5, 0.8, 0.9, 0.5},
                        {0.9, 0.65, 0.8, 0.1, 0.3}
                }
        };

        double[] input = new double[] {6, 4, 5, 8, 1};
        double[] expectedOutput = new double[]{0.5478, 0.56, 0.5676, 0.5545};

        nn.setUpWeights(body);
        double[] output = nn.respond(input);
        Assert.assertArrayEquals(expectedOutput, output, 0.01);
    }

    @Test
    public void calculateErrors() throws Exception {
    }

}