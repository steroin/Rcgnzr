package databuilder;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSetSerializer {

    private ErrorHandler errorHandler;

    public DataSetSerializer(ErrorHandler handler) {
        errorHandler = handler;
    }

    public void serialize(DataSet dataSet, File destination) {

        if (destination == null || !destination.exists()) {
            errorHandler.handle(new Error("DataSetSerializer error: destination file not found.", ErrorType.IMPORTANT));
            return;
        }
        if (destination.isDirectory()) {
            errorHandler.handle(new Error("DataSetSerializer error: cannot serialize directory.", ErrorType.IMPORTANT));
            return;
        }

        ArrayList<double[]> features = dataSet.getFeatures();
        ArrayList<Double> classes = dataSet.getClassifications();

        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination))) {
                for (int i = 0; i < features.size(); i++) {
                    double[] vector = features.get(i);

                    for (int j = 0; j < vector.length; j++) {
                        bw.write(vector[j]+"");
                        if (j < vector.length - 1) bw.write(";");
                    }
                    bw.write(" "+classes.get(i));
                    bw.newLine();
                }
            }
        } catch(IOException e) {
            errorHandler.handle(new Error("DataSetSerializer error: cannot access destination file.", ErrorType.IMPORTANT));
        }
    }

    public DataSet deserialize(File source) {

    }
}
