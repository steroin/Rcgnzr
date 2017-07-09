package loader.file;

import loader.error.ErrorHandler;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class FileLoaderTest {
    @Test
    public void loadFromFile() throws Exception {
        FileLoader loader = new FileLoader(ErrorHandler.getInstance());
        BufferedImage src = loader.loadFromFile("C:/Users/Sergiusz/Desktop/java.png");
        Assert.assertNotNull(src);
    }

    @Test
    public void loadFromInternalResource() throws Exception {
        FileLoader loader = new FileLoader(ErrorHandler.getInstance());
        BufferedImage src = loader.loadFromInternalResource("/loader/file/java.png");
        Assert.assertNotNull(src);
    }

}