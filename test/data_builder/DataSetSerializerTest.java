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
    private File source;

    @Before
    public void setUp() {
        int classes = 4;
        int vectorSize = 20;
        DataSet set = new DataSet(classes, vectorSize, ErrorHandler.getInstance());


        for (int i = 0; i < 10; i++) {
            double[] vector = new double[vectorSize];
            for (int j = 0; j < vectorSize; j++) {
                vector[j] = Math.random()*100;
            }
            try {
                set.addData(vector, (int) (Math.random()*(classes-1))+1);
            } catch (InvalidDataFormatException e) {
                e.printStackTrace();
            }
        }
        dataSet = set;
        source = new File(getClass().getResource("/databuilder/example_data_source.txt").getPath());
    }

    @Test
    public void equals() {
        Assert.assertTrue(dataSet.equals(dataSet));
    }

    @Test
    public void serializeAndDeserialize() {
        DataSetSerializer serializer = new DataSetSerializer(ErrorHandler.getInstance());
        serializer.serialize(dataSet, source);
        DataSet set = serializer.deserialize(source);
        Assert.assertTrue(dataSet.equals(set));
    }

    @After
    public void clearSource() {
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(source))) {
                bw.write("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}