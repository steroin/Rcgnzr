package data_builder;

import loader.error.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSet {
    private List<double[]> features;
    private List<Double> classes;
    private ErrorHandler errorHandler;

    public DataSet(List<double[]> featuresList, List<Double> classesList, ErrorHandler handler) {
        features = featuresList;
        classes = classesList;
        errorHandler = handler;
    }

    public DataSet(ErrorHandler handler) {
        this(new ArrayList<>(), new ArrayList<>(), handler);
    }

    public void addData(double[] featureVector, double classification) {
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

            for (int j = 0; j < vector.length; j++) {
                if (vector[j] != myVector[j]) return false;
            }
        }

        for (int i = 0; i < classes.size(); i++) {
            if (!classes.get(i).equals(setClasses.get(i))) return false;
        }

        return true;
    }

    public void merge(DataSet set) {
        List<Double> setClasses = set.getClassifications();
        features.addAll(set.getFeatures());
        classes.addAll(setClasses);
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
