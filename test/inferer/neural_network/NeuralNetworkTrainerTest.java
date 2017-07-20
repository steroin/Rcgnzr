package inferer.neural_network;

import data_builder.DataSet;
import data_builder.DataSetSerializer;
import loader.error.ErrorHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sergiusz on 16.07.2017.
 */
public class NeuralNetworkTrainerTest {
    private DataSet trainingSet;
    private DataSet validationSet;


    @Before
    public void prepare() {
        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        trainingSet = serializer.deserialize(new File(getClass().getResource("/inferer/neural_network/training2.txt").getPath()));
        validationSet = serializer.deserialize(new File(getClass().getResource("/inferer/neural_network/validation2.txt").getPath()));

    }
    @Test
    public void train() throws Exception {
        NeuralNetwork neuralNetwork = new NeuralNetwork(8, 2, new int[]{4, 2});
        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(neuralNetwork, trainingSet, validationSet, null);

        double[] params = trainer.chooseParameters(new double[]{0, 0.000001, 0.001, 0.01, 0.1, 0.2, 0.5, 1}, new double[]{0, 0.000001, 0.001, 0.01, 0.1, 0.5, 1}, new double[]{0, 0.000001, 0.001, 0.01, 0.1, 1});
        //double[] params = trainer.chooseParameters(new double[]{1}, new double[]{1}, new double[]{1});
    }

}