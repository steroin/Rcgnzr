package data_builder;

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


        try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination))) {
            for (int i = 0; i < features.size(); i++) {
                double[] vector = features.get(i);

                for (int j = 0; j < vector.length; j++) {
                    bw.write(vector[j]+"");
                    if (j < vector.length - 1) bw.write(";");
                }
                bw.write(";"+classes.get(i));
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access destination file.", ErrorType.IMPORTANT));
        }
    }

    public DataSet deserialize(File source) {

        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            int vectorSize = 0;
            int classesNum = 0;
            ArrayList<double[]> features = new ArrayList<>();
            ArrayList<Double> classes = new ArrayList<>();

            String line = br.readLine();
            while (line != null) {
                String[] vector = line.split(";");
                double[] parsedVector = new double[vector.length - 1];
                double cls = Double.parseDouble(vector[vector.length - 1]);

                for (int i = 0; i < vector.length - 1; i++) {
                    parsedVector[i] = Double.parseDouble(vector[i]);
                }
                if (vectorSize == 0) {
                    vectorSize = parsedVector.length;
                }
                if (!classes.contains(cls)) {
                    classesNum ++;
                }

                features.add(parsedVector);
                classes.add(cls);
                line = br.readLine();
            }
            return new DataSet(classesNum, vectorSize, features, classes, errorHandler);
        } catch (IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access source file", ErrorType.IMPORTANT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: source file has wrong format"));
        }
        return null;
    }
}
