package data_builder.images.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    private File dataSetInfoFile;
    private DataSet dataSet;
    private List<String> assignedFiles;
    private boolean isDataSetEmpty;


    public DataSetBuilder(String path, String infoPath) {
        dataSetFile = new File(path);
        dataSetInfoFile = new File(infoPath);
        assignedFiles = new ArrayList<>();
        isDataSetEmpty = true;

        try {
            dataSetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet file: "+path, ErrorType.CRITICAL));
        }

        try {
            dataSetInfoFile.createNewFile();
        }  catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet info file: "+path, ErrorType.CRITICAL));
        }

        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        dataSet = serializer.deserialize(dataSetFile);

        if (dataSet.getFeatures().size() > 0) {
            isDataSetEmpty = false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(dataSetInfoFile))) {
            String line = br.readLine();
            while (line != null) {
                assignedFiles.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEntry(double[] features, double classification, String fileName) {
        if (isDataSetEmpty()) {
            initializeDataSet(features.length, 6);
        }

        try {
            dataSet.addData(features, classification);
        } catch (InvalidDataFormatException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataSetFile, true))) {
            if (!isDataSetEmpty()) {
                bw.newLine();
            }
            for (int i = 0; i < features.length; i++) {
                bw.append(features[i] + "");
                if (i < features.length - 1) bw.append(";");
            }
            bw.append(";" + classification);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataSetInfoFile, true))) {
            if (!isDataSetEmpty()) {
                bw.newLine();
            }
            bw.append(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isDataSetEmpty = false;
        assignedFiles.add(fileName);
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public List<String> getAssignedFilesList() {
        return assignedFiles;
    }

    public boolean isDataSetEmpty() {
        return isDataSetEmpty;
    }

    private void initializeDataSet(int featureSize, int classesNum) {
        dataSet = new DataSet(classesNum, featureSize, ErrorHandler.getInstance());
    }
}
