package databuilder;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import java.util.ArrayList;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSet {
    private int classesNum;
    private int featureVectorSize;
    private ArrayList<double[]> features;
    private ArrayList<Double> classes;
    private ErrorHandler errorHandler;

    public DataSet(int c, int fvs, ArrayList<double[]> featuresList, ArrayList<Double> classesList, ErrorHandler handler) {
        classesNum = c;
        featureVectorSize = fvs;
        features = featuresList;
        classes = classesList;
        errorHandler = handler;
    }

    public DataSet(int c, int fvs, ErrorHandler handler) {
        this(c, fvs, new ArrayList<>(), new ArrayList<>(), handler);
    }

    public void addData(double[] featureVector, double classification) throws InvalidDataFormatException {
        if (featureVector.length != featureVectorSize) {
            throw new InvalidDataFormatException(String.format("Data vector size (%d) doesnt match required size: %d", featureVector.length, featureVectorSize));
        }

        features.add(featureVector);
        classes.add(classification);
    }

    public ArrayList<double[]> getFeatures() {
        return features;
    }

    public ArrayList<Double> getClassifications() {
        return classes;
    }

    public boolean equals(DataSet set) {
        if (set == null) return false;
        ArrayList<double[]> setFeaturesVector = set.getFeatures();
        ArrayList<Double> setClasses = set.getClassifications();

        if (setFeaturesVector.size() != features.size()) return false;
        if (setClasses.size() != classes.size()) return false;

        for (int i = 0; i < features.size(); i++) {
            double[] vector = setFeaturesVector.get(i);
            double[] myVector = features.get(i);

            for (int j = 0; j < featureVectorSize; j++) {
                if (vector[j] != myVector[j]) return false;
            }
        }

        for (int i = 0; i < classes.size(); i++) {
            if (!classes.get(i).equals(setClasses.get(i))) return false;
        }

        return true;
    }

    public void merge(DataSet set) {
        if (set.featureVectorSize != featureVectorSize) {
            errorHandler.handle(new Error("DataSet merge error: target DataSet has different size", ErrorType.IMPORTANT));
            return;
        }
        ArrayList<Double> setClasses = set.getClassifications();
        for (double cls : setClasses) {
            if (!classes.contains(cls)) {
                classesNum++;
            }
        }

        features.addAll(set.getFeatures());
        classes.addAll(setClasses);
    }
}
