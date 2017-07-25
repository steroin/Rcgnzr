package data_builder.images.logic;

import loader.error.Error;
import loader.error.ErrorHandler;
import loader.error.ErrorType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiusz on 20.07.2017.
 */
public class ImageManager {
    private BufferedImage source;
    private List<File> listOfFiles;
    private int currentFileIndex;
    private final String[] imagesExtensions = new String[] {"png", "jpg", "jpeg", "gif", "pgm"};
    private int preferredWidth;
    private int preferredHeight;

    public ImageManager(String sourcePath, int width, int height) {
        File sourceDirectory = new File(sourcePath);
        listOfFiles = new ArrayList<>();
        for (File f : sourceDirectory.listFiles()) {
            if (hasFileValidImageExtension(f)) {
                listOfFiles.add(f);
            }
        }
        currentFileIndex = 0;
        preferredWidth = width;
        preferredHeight = height;
    }

    public void next() {
        do {
            currentFileIndex++;
            if (currentFileIndex >= listOfFiles.size()) {
                currentFileIndex = 0;
            }
        } while (!hasFileValidImageExtension(listOfFiles.get(currentFileIndex)));
        reloadImage();
    }

    public void previous() {
        do {
            currentFileIndex--;
            if (currentFileIndex < 0) {
                currentFileIndex = listOfFiles.size() - 1;
            }
        } while (!hasFileValidImageExtension(listOfFiles.get(currentFileIndex)));
        reloadImage();
    }

    public void init() {
        currentFileIndex = 0;
        reloadImage();
    }

    private void reloadImage() {
        File f = listOfFiles.get(currentFileIndex);
        try {
            source = scaleToProperSize(ImageIO.read(f));
        } catch (IOException e) {
            ErrorHandler.getInstance().handle(new Error("Cannot load image file", ErrorType.CRITICAL));
            e.printStackTrace();
        }
    }

    private BufferedImage scaleToProperSize(BufferedImage source) {
        BufferedImage resized = new BufferedImage(preferredWidth, preferredHeight, source.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, preferredWidth, preferredHeight, 0, 0, source.getWidth(),
                source.getHeight(), null);
        g.dispose();
        return resized;
    }

    private boolean hasFileValidImageExtension(File f) {
        String[] splitName = f.getName().split("\\.");
        String ext = splitName[splitName.length - 1];
        return isValidImageExtension(ext);
    }

    private boolean isValidImageExtension(String extension) {
        for (String ext : imagesExtensions) {
            if (ext.equals(extension)) return true;
        }
        return false;
    }

    public int getImagesNum() {
        int count = 0;
        for (File f : listOfFiles) {
            if (hasFileValidImageExtension(f)){
                count++;
            }
        }
        return count;
    }

    public BufferedImage getImage() {
        return source;
    }

    public String getCurrentImageName() {
        return listOfFiles.get(currentFileIndex).getName();
    }

    public int getCurrentIndex() {
        return currentFileIndex;
    }
}
