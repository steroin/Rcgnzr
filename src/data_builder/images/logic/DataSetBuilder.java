package data_builder.images.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import data_builder.DataSet;
import data_builder.DataSetSerializer;
import data_builder.InvalidDataFormatException;
import loader.error.ErrorHandler;
import loader.error.ErrorType;
import loader.error.Error;

/**
 * Created by Sergiusz on 21.07.2017.
 */
public class DataSetBuilder {
    private File dataSetFile;
    private DataSet dataSet;

    public DataSetBuilder(String path) {
        dataSetFile = new File(path);
        try {
            dataSetFile.createNewFile();
        } catch (IOException e) {
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet file: "+path, ErrorType.CRITICAL));
        }

        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        dataSet = serializer.deserialize(dataSetFile);
    }

    public void addEntry(double[] features, double classification) {
        try {
            dataSet.addData(features, classification);
        } catch (InvalidDataFormatException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataSetFile))) {
            bw.newLine();
            for (int i = 0; i < features.length; i++) {
                bw.append(features[i]+"");
                if (i < features.length - 1) bw.append(";");
            }
            bw.append(";"+classification);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));
        }
    }

    public DataSet getDataSet() {
        return dataSet;
    }
}
