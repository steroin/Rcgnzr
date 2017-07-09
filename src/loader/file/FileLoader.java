package loader.file;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Sergiusz on 07.07.2017.
 */
public class FileLoader {
    private ErrorHandler errorHandler;

    public FileLoader(ErrorHandler handler) {
        errorHandler = handler;
    }

    public BufferedImage loadFromFile(String absolutePath) {
        File img = new File(absolutePath);
        BufferedImage src = null;

        try {
            src = ImageIO.read(img);
        } catch (IOException e) {
            errorHandler.handle(new Error("Unable to read file: "+absolutePath, ErrorType.IMPORTANT));
        }

        return src;
    }

    public BufferedImage loadFromInternalResource(String projectPath) {
        URL url = getClass().getResource(projectPath);
        File img = new File(url.getPath());

        BufferedImage src = null;

        try {
            src = ImageIO.read(img);
        } catch (IOException e) {
            errorHandler.handle(new Error("Unable to read file: "+projectPath, ErrorType.IMPORTANT));
        }

        return src;
    }
}
