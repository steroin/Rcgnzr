package data_builder;

import loader.error.ErrorHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Sergiusz on 13.07.2017.
 */
public class DataSetTest {
    @Test
    public void equals() throws Exception {
        DataSet setOne = new DataSet(3, 5, ErrorHandler.getInstance());
        setOne.addData(new double[]{1.3, 65.664, 15.812, 8.2391, 4.1231}, 1.0);
        setOne.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setOne.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);
        DataSet setTwo = new DataSet(3, 5, ErrorHandler.getInstance());
        setTwo.addData(new double[]{1.3, 65.664, 15.812, 8.2391, 4.1231}, 1.0);
        setTwo.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setTwo.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);

        Assert.assertTrue(setOne.equals(setTwo));
        Assert.assertTrue(setTwo.equals(setOne));

        DataSet setThree = new DataSet(3, 5, ErrorHandler.getInstance());
        setThree.addData(new double[]{1.3, 65.664, 15.822, 8.2391, 4.1231}, 1.0);
        setThree.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setThree.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);

        Assert.assertFalse(setThree.equals(setOne));
        Assert.assertFalse(setOne.equals(setThree));

        DataSet setFour = new DataSet(3, 5, ErrorHandler.getInstance());
        setFour.addData(new double[]{1.3, 65.664, 15.822, 8.2391, 4.1231}, 1.0);
        setFour.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setFour.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);
        setFour.addData(new double[]{11.3412, 72.123, 3.2144, 4.21312, 9.21312}, 3.0);

        Assert.assertFalse(setFour.equals(setThree));
        Assert.assertFalse(setThree.equals(setFour));
    }

    @Test
    public void merge() throws Exception {
        DataSet setOne = new DataSet(3, 5, ErrorHandler.getInstance());
        setOne.addData(new double[]{1.3, 65.664, 15.812, 8.2391, 4.1231}, 1.0);
        setOne.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setOne.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);
        DataSet setTwo = new DataSet(3, 5, ErrorHandler.getInstance());
        setTwo.addData(new double[]{14.162, 82.313, 81.23123, 0.231, 21.3212}, 4.0);
        setTwo.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 1.0);
        DataSet setThree = new DataSet(3, 5, ErrorHandler.getInstance());
        setThree.addData(new double[]{1.3, 65.664, 15.812, 8.2391, 4.1231}, 1.0);
        setThree.addData(new double[]{123.231, 5.2317, 7.7712, 67.4812, 19.1963}, 2.0);
        setThree.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 3.0);
        setThree.addData(new double[]{14.162, 82.313, 81.23123, 0.231, 21.3212}, 4.0);
        setThree.addData(new double[]{32.21312, 712.12321, 91.2312, 17.231, 19.2312}, 1.0);

        setOne.merge(setTwo);
        Assert.assertTrue(setThree.equals(setOne));
    }

}