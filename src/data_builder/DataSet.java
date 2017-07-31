package data_builder;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSet {
    private int classesNum;
    private int featureVectorSize;
    private List<double[]> features;
    private List<Double> classes;
    private ErrorHandler errorHandler;

    public DataSet(int c, int fvs, List<double[]> featuresList, List<Double> classesList, ErrorHandler handler) {
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

    public List<double[]> getFeatures() {
        return features;
    }

    public List<Double> getClassifications() {
        return classes;
    }

    public boolean equals(DataSet set) {
        if (set == null) return false;
        List<double[]> setFeaturesVector = set.getFeatures();
        List<Double> setClasses = set.getClassifications();

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
        List<Double> setClasses = set.getClassifications();
        for (double cls : setClasses) {
            if (!classes.contains(cls)) {
                classesNum++;
            }
        }

        features.addAll(set.getFeatures());
        classes.addAll(setClasses);
    }

    public int getClassesNum() {
        return classesNum;
    }

    public int getFeatureVectorSize() {
        return featureVectorSize;
    }

    public void shuffle() {
        int length = features.size();
        int ranIndex;
        double[] tempFeatures;
        double tempCls;

        for (int i = 0; i < length - 2; i++) {
            ranIndex = (int) (Math.random() * (length - i) + i);
            tempFeatures = features.get(ranIndex);
            features.set(ranIndex, features.get(i));
            features.set(i, tempFeatures);
            tempCls = classes.get(ranIndex);
            classes.set(ranIndex, classes.get(i));
            classes.set(i, tempCls);
        }

    }
}
