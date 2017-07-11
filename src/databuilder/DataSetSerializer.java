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
            try (FileWriter fw = new FileWriter(destination)) {
            }
        } catch(IOException e) {
            errorHandler.handle(new Error("DataSetSerializer error: cannot access destination file.", ErrorType.IMPORTANT));
        }
    }

    public DataSet deserialize(File source) {
        return null;
    }
}
