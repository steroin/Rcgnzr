package data_builder;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSetSerializer {

    private ErrorHandler errorHandler;

    public DataSetSerializer(ErrorHandler handler) {
        errorHandler = handler;
    }

    public void serializeClassifications(List<Double> classifications, File destination) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination))) {
            for (double d : classifications) {
                bw.write(d+"");
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access destination file.", ErrorType.IMPORTANT));
        }
    }

    public List<Double> deserializeClassifications(File source) {
        List<Double> classes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line = br.readLine();
            while (line != null) {
                double cls = Double.parseDouble(line);
                classes.add(cls);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access source file", ErrorType.IMPORTANT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: source file has wrong format"));
        }
        return classes;
    }

    public void serializeFeatureSet(List<double[]> featureSet, File destination) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(destination))) {
            for (int i = 0; i < featureSet.size(); i++) {
                double[] vector = featureSet.get(i);

                for (int j = 0; j < vector.length; j++) {
                    bw.write(vector[j]+"");
                    if (j < vector.length - 1) bw.write(";");
                }
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access destination file.", ErrorType.IMPORTANT));
        }
    }

    public List<double[]> deserializeFeatureSet(File source) {
        List<double[]> features = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            String line = br.readLine();
            while (line != null) {
                String[] vector = line.split(";");
                double[] parsedVector = new double[vector.length];

                for (int i = 0; i < vector.length; i++) {
                    parsedVector[i] = Double.parseDouble(vector[i]);
                }

                features.add(parsedVector);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: cannot access source file", ErrorType.IMPORTANT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorHandler.handle(new Error("DataSetSerializer error: source file has wrong format"));
        }
        return features;
    }

    public void serialize(DataSet dataSet, File featuresDestination, File classesDestination) {
        serializeFeatureSet(dataSet.getFeatures(), featuresDestination);
        serializeClassifications(dataSet.getClassifications(), classesDestination);
    }

    public DataSet deserialize(File featuresSource, File classesSource) {
        List<Double> classes = deserializeClassifications(classesSource);
        List<double[]> features = deserializeFeatureSet(featuresSource);
        List<Double> distinctClasses = new ArrayList<>();

        for (double cls : classes) {
            if (!distinctClasses.contains(cls)) {
                distinctClasses.add(cls);
            }
        }

        int featureVectorSize = 0;
        if (features != null && !features.isEmpty()) {
            featureVectorSize = features.get(0).length;
        }
        return new DataSet(distinctClasses.size(), featureVectorSize, features, classes, ErrorHandler.getInstance());
    }
}
