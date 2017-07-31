package data_builder.images.logic;

import data_builder.DataSet;
import data_builder.DataSetSerializer;
import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 21.07.2017.
 */
public class DataSetBuilder {
    private File dataSetFeaturesFile;
    private File dataSetClassesFile;
    private File dataSetInfoFile;
    private DataSet dataSet;
    private List<String> assignedFiles;
    private boolean isDataSetEmpty;


    public DataSetBuilder(String featuresPath, String classesPath, String infoPath) {
        dataSetFeaturesFile = new File(featuresPath);
        dataSetClassesFile = new File(classesPath);
        dataSetInfoFile = new File(infoPath);
        assignedFiles = new ArrayList<>();
        isDataSetEmpty = true;

        try {
            dataSetFeaturesFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet features file. "+featuresPath, ErrorType.CRITICAL));
        }
        try {
            dataSetClassesFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet classes file. "+classesPath, ErrorType.CRITICAL));
        }
        try {
            dataSetInfoFile.createNewFile();
        }  catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot load DataSet info file: "+infoPath, ErrorType.CRITICAL));
        }

        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        dataSet = serializer.deserialize(dataSetFeaturesFile, dataSetClassesFile);

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
        dataSet.addData(features, classification);
        ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataSetFeaturesFile, true))) {
            if (!isDataSetEmpty()) {
                bw.newLine();
            }
            for (int i = 0; i < features.length; i++) {
                bw.append(features[i] + "");
                if (i < features.length - 1) bw.append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ErrorHandler.getInstance().handle(new Error("Cannot add data to existing DataSet", ErrorType.IMPORTANT));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataSetClassesFile, true))) {
            if (!isDataSetEmpty()) {
                bw.newLine();
            }
            bw.append(classification+"");
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

    private void initializeDataSet() {
        dataSet = new DataSet(ErrorHandler.getInstance());
    }
}
