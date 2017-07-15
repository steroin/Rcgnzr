package descriptor.hog;

import loader.error.ErrorHandler;
import loader.file.FileLoader;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class HOGDescriptorTest {
    @Test
    public void describe() throws Exception {
        FileLoader loader = new FileLoader(ErrorHandler.getInstance());
        BufferedImage src = loader.loadFromInternalResource("/descriptor/hog/test_hog_5.png");
        HOGDescriptor descriptor = new HOGDescriptor(src, null);
        long start = System.currentTimeMillis();
        double[] hog = descriptor.describe().getAsVector();
        long stop = System.currentTimeMillis();

        System.out.println((stop-start)+" ms");
    }

    @Test
    public void countPolarCoordinates() throws Exception {
    }

    @Test
    public void getHistogram() throws Exception {

    }

    @Test
    public void calculateFeatures() throws Exception {
    }

    @Test
    public void calculateBlockHistograms() throws Exception {
    }


}