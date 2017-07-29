package descriptor.hog;

import descriptor.FeatureDescriptionResult;
import descriptor.FeatureDescriptor;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class HOGDescriptor implements FeatureDescriptor {
    private BufferedImage source;
    private int width;
    private int height;
    private int cellWidth;
    private int cellHeight;
    private int blockWidth;
    private int blockHeight;
    private int histogramLength;

    public HOGDescriptor(BufferedImage src, HOGSettings settings) {
        source = src;
        width = source.getWidth();
        height = source.getHeight();
        cellWidth = settings.getCellWidth();
        cellHeight = settings.getCellHeight();
        blockWidth = settings.getBlockWidth();
        blockHeight = settings.getBlockHeight();
        histogramLength = settings.isSignedGradients() ? 18 : 9;
    }

    public double[][][] calculateCellHistograms(int width, int height, double[][] magnitudes, double[][] directions) {
        double[][][] histograms = new double[height / cellHeight][width / cellWidth][histogramLength];

        for (int i = 0; i < height / cellHeight; i++) {
            for (int j = 0; j < width / cellWidth; j++) {
                histograms[i][j] = getHistogram(i, j, magnitudes, directions);
            }
        }
        return histograms;
    }

    public double[] calculateFeatures(int width, int height, double[][][] histograms) {
        int singleBlockFeatureLength = blockWidth * blockHeight * histogramLength;
        double[] features = new double[(height / cellHeight - 1) * (width / cellWidth - 1) * singleBlockFeatureLength];
        int index = 0;

        for (int i = 0; i < height / cellHeight - 1; i++) {
            for (int j = 0; j < width / cellWidth - 1; j++) {
                double[] localFeature = new double[singleBlockFeatureLength];

                for (int k = 0; k < blockHeight; k++) {
                    for (int l = 0; l < blockWidth; l++) {
                        for (int m = 0; m < histogramLength; m++) {
                            localFeature[(k * blockWidth + l) * histogramLength + m] = histograms[i + k][j + l][m];
                        }
                    }
                }

                double length = 0;
                for (int k = 0; k < singleBlockFeatureLength; k++) {
                    length += localFeature[k]*localFeature[k];
                }
                length = Math.sqrt(length);

                for (int k = 0; k < singleBlockFeatureLength; k++) {
                    features[index] = length == 0 ? 0 : localFeature[k] / length;
                    index++;
                }
            }
        }
        return features;
    }

    public double[][][] countPolarMatrices(int width, int height, BufferedImage source) {
        double[][] magnitudes = new double[height][width];
        double[][] directions = new double[height][width];

        for (int i = 0; i < height; i++) {
            boolean hasTop = i - 1 >= 0;
            boolean hasBottom = i + 1 < height;

            for (int j = 0; j < width; j++) {
                int top = ((hasTop ? source.getRGB(j, i - 1) >> 16 & 0xFF : 0) +
                        (hasTop ? source.getRGB(j, i - 1) >> 8 & 0xFF : 0) +
                        (hasTop ? source.getRGB(j, i - 1) & 0xFF : 0))/3;
                int bottom = ((hasBottom ? source.getRGB(j, i + 1) >> 16 & 0xFF : 0) +
                        (hasBottom ? source.getRGB(j, i + 1) >> 8 & 0xFF : 0) +
                        (hasBottom ? source.getRGB(j, i + 1) & 0xFF : 0))/3;
                int left = ((j - 1 >= 0 ? source.getRGB(j - 1, i) >> 16 & 0xFF : 0) +
                        (j - 1 >= 0 ? source.getRGB(j - 1, i) >> 8 & 0xFF : 0) +
                        (j - 1 >= 0 ? source.getRGB(j - 1, i) & 0xFF : 0))/3;
                int right = ((j + 1 < width ? source.getRGB(j + 1, i) >> 16 & 0xFF : 0) +
                        (j + 1 < width ? source.getRGB(j + 1, i) >> 8 & 0xFF : 0) +
                        (j + 1 < width ? source.getRGB(j + 1, i) & 0xFF : 0))/3;

                int hotizontalDeriv = right - left;
                int verticalDeriv = bottom - top;

                int maxDirection = histogramLength * 20;
                magnitudes[i][j] = Math.sqrt(hotizontalDeriv * hotizontalDeriv + verticalDeriv * verticalDeriv);
                directions[i][j] = (Math.toDegrees(Math.atan2(verticalDeriv, hotizontalDeriv)) + maxDirection) % maxDirection;
            }
        }

        return new double[][][]{magnitudes, directions};
    }

    @Override
    public FeatureDescriptionResult describe() {
        double[][][] polarMatrices = countPolarMatrices(width, height, source);
        double[][] magnitudes = polarMatrices[0];
        double[][] directions = polarMatrices[1];
        double[][][] blockHistograms = calculateCellHistograms(width, height, magnitudes, directions);
        double[] features = calculateFeatures(width, height, blockHistograms);
        return new FeatureDescriptionResult(features);
    }

    @Override
    public void setSource(BufferedImage image) {
        source = image;
        width = source.getWidth();
        height = source.getHeight();
    }

    public double[] getHistogram(int cellX, int cellY, double[][] magnitudes, double[][] directions) {
        double[] histogram = new double[histogramLength];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < cellHeight; i++) {
            for (int j = 0; j < cellWidth; j++) {
                int x = cellX * cellHeight + i;
                int y = cellY * cellWidth + j;
                int histogramIndex = (int) (directions[x][y] / 20);
                histogram[histogramIndex] = histogram[histogramIndex] + (1 - (directions[x][y] % 20) / 20) * magnitudes[x][y];
                histogram[(histogramIndex + 1) % histogram.length] = ((directions[x][y] % 20) / 20) * magnitudes[x][y];
            }
        }
        return histogram;
    }

}
