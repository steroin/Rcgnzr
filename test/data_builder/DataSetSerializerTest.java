package data_builder;

import loader.error.ErrorHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Created by Sergiusz on 11.07.2017.
 */
public class DataSetSerializerTest {
    private DataSet dataSet;
    private File sourceFeatures;
    private File sourceClasses;

    @Before
    public void setUp() {
        int vectorSize = 20;
        DataSet set = new DataSet(ErrorHandler.getInstance());
        double[] classifications = new double[]{1, 3, 4, 1, 3, 2, 1, 2, 4, 1};

        for (int i = 0; i < 10; i++) {
            double[] vector = new double[vectorSize];
            for (int j = 0; j < vectorSize; j++) {
                vector[j] = Math.random()*100;
            }
            set.addData(vector, classifications[i]);
        }
        dataSet = set;
        sourceFeatures = new File(getClass().getResource("/data_builder/example_data_source_ft.txt").getPath());
        sourceClasses = new File(getClass().getResource("/data_builder/example_data_source_cls.txt").getPath());
    }

    @Test
    public void equals() {
        Assert.assertTrue(dataSet.equals(dataSet));
    }

    @Test
    public void serializeAndDeserialize() {
        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        serializer.serialize(dataSet, sourceFeatures, sourceClasses);
        DataSet set = serializer.deserialize(sourceFeatures, sourceClasses);
        Assert.assertTrue(dataSet.equals(set));
    }

    @After
    public void clearSource() {
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(sourceFeatures))) {
                bw.write("");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(sourceClasses))) {
                bw.write("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}