package descriptor.hog;

import descriptor.FeatureData;
import descriptor.FeatureDescriptor;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class HOGDescriptor implements FeatureDescriptor {
    private HOGSettings settings;
    private final BufferedImage source;
    private double[][] magnitudes;
    private double[][] directions;

    public HOGDescriptor(BufferedImage src) {
        source = src;
    }

    private void countPolarCoordinates(int width, int height) {
        magnitudes = new double[height][width];
        directions = new double[height][width];

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

                magnitudes[i][j] = Math.sqrt(hotizontalDeriv * hotizontalDeriv + verticalDeriv * verticalDeriv);
                directions[i][j] = (Math.toDegrees(Math.atan2(verticalDeriv, hotizontalDeriv)) + 180) % 180;
            }
        }
    }

    private double[] getHistogram(int row, int col) {
        double[] histogram = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int x = row * 8 + i;
                int y = col * 8 + j;
                int histogramIndex = (int) (directions[x][y] / 20);
                histogram[histogramIndex] = histogram[histogramIndex] + (1 - (directions[x][y] % 20) / 20) * magnitudes[x][y];
                histogram[(histogramIndex + 1) % histogram.length] = ((directions[x][y] % 20) / 20) * magnitudes[x][y];
            }
        }
        return histogram;
    }

    private double[] calculateFeatures(int width, int height, double[][][] histograms) {
        double[] features = new double[(height / 8 - 1) * (width / 8 - 1) * 36];
        int index = 0;
        for (int i = 0; i < height / 8 - 1; i++) {
            for (int j = 0; j < width / 8 - 1; j++) {
                double[] localFeature = new double[36];

                for (int k = 0; k < 8; k++) localFeature[k] = histograms[i][j][k];
                for (int k = 0; k < 8; k++) localFeature[k+8] = histograms[i][j+1][k];
                for (int k = 0; k < 8; k++) localFeature[k+16] = histograms[i+1][j][k];
                for (int k = 0; k < 8; k++) localFeature[k+24] = histograms[i+1][j+1][k];

                double length = 0;
                for (int k = 0; k <36; k++) length += localFeature[k]*localFeature[k];
                length = Math.sqrt(length);
                for (int k = 0; k <36; k++) {
                    features[index] = localFeature[k] / length;
                    index++;
                }
            }
        }
        return features;
    }
    @Override
    public FeatureData describe() {
        int width = source.getWidth();
        int height = source.getHeight();

        countPolarCoordinates(width, height);

        double[][][] histograms = new double[height / 8][width / 8][9];
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width / 8; j++) {
                histograms[i][j] = getHistogram(i, j);
            }
        }

        double[] features = calculateFeatures(width, height, histograms);
        return new FeatureData(features);
    }

    @Override
    public BufferedImage getSource() {
        return source;
    }
}
