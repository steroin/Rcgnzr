package data_builder;

import descriptor.hog.HOGDescriptor;
import descriptor.hog.HOGSettings;
import inferer.neural_network.NeuralNetwork;
import inferer.neural_network.NeuralNetworkTrainer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 29.07.2017.
 */
public class MouthDetector {
    private NeuralNetwork neuralNetwork;
    private NeuralNetworkTrainer trainer;
    private DataSet dataSet;

    public MouthDetector(NeuralNetwork neuralNetwork, NeuralNetworkTrainer trainer, DataSet dataSet) {
        this.neuralNetwork = neuralNetwork;
        this.trainer = trainer;
        this.dataSet = dataSet;
    }

    public void initDataSet(String positiveExampleDirectoryPath, String negativeExampleDirectoryPath) {
        File positiveExampleDirectory = new File(positiveExampleDirectoryPath);
        File negativeExampleDirectory = new File(negativeExampleDirectoryPath);

        HOGDescriptor descriptor = new HOGDescriptor(null, HOGSettings.DEFAULT);
        List<Double> classes = new ArrayList<>();
        List<double[]> features = new ArrayList<>();
        BufferedImage img;

        try {
            for (File f : positiveExampleDirectory.listFiles()) {
                img  = ImageIO.read(f);
                descriptor.setSource(img);
                features.add(descriptor.describe().getAsVector());
                classes.add(1.0);
            }

            for (File f : negativeExampleDirectory.listFiles()) {
                img  = ImageIO.read(f);
                descriptor.setSource(img);
                features.add(descriptor.describe().getAsVector());
                classes.add(0.0);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < features.size(); i++) {
            try {
                dataSet.addData(features.get(i), classes.get(i));
            } catch (InvalidDataFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
