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

    public HOGDescriptor(BufferedImage src) {
        source = src;
    }

    @Override
    public FeatureData describe() {
        int width = source.getWidth();
        int height = source.getHeight();

        double[][] magnitudes = new double[width][height];
        double[][] directions = new double[width][height];

        for (int i = 0; i < height; i++) {
            boolean hasTop = i - 1 >= 0;
            boolean hasBottom = i + 1 < height;

            for (int j = 0; j < width; j++) {
                int top = ((hasTop ? source.getRGB(i - 1, j) >> 16 & 0xFF : 0) +
                        (hasTop ? source.getRGB(i - 1, j) >> 8 & 0xFF : 0) +
                        (hasTop ? source.getRGB(i - 1, j) & 0xFF : 0))/3;
                int bottom = ((hasBottom ? source.getRGB(i + 1, j) >> 16 & 0xFF : 0) +
                        (hasBottom ? source.getRGB(i + 1, j) >> 8 & 0xFF : 0) +
                        (hasBottom ? source.getRGB(i + 1, j) & 0xFF : 0))/3;
                int left = ((j - 1 >= 0 ? source.getRGB(i, j - 1) >> 16 & 0xFF : 0) +
                        (j - 1 >= 0 ? source.getRGB(i, j - 1) >> 8 & 0xFF : 0) +
                        (j - 1 >= 0 ? source.getRGB(i, j - 1) & 0xFF : 0))/3;
                int right = ((j + 1 < width ? source.getRGB(i, j + 1) >> 16 & 0xFF : 0) +
                        (j + 1 < width ? source.getRGB(i, j + 1) >> 8 & 0xFF : 0) +
                        (j + 1 < width ? source.getRGB(i, j + 1) & 0xFF : 0))/3;

                int hotizontalDeriv = right - left;
                int verticalDeriv = bottom - top;

                magnitudes[i][j] = Math.sqrt(hotizontalDeriv * hotizontalDeriv + verticalDeriv * verticalDeriv);
                directions[i][j] = (Math.toDegrees(Math.atan2(verticalDeriv, hotizontalDeriv)) + 180) % 180;
            }
        }

        double[][][] histograms = new double[height / 8][width / 8][9];
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width / 8; j++) {

                double[] histogram = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        int x = i + k;
                        int y = j + l;
                        int histogramIndex = (int) (directions[x][y] / 20);
                        histogram[histogramIndex] = histogram[histogramIndex] + (1 - (directions[x][y] % 20) / 20) * magnitudes[x][y];
                        histogram[(histogramIndex + 1) % histogram.length] = ((directions[x][y] % 20) / 20) * magnitudes[x][y];
                    }
                }
                histograms[i][j] = histogram;
            }
        }

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
        return new FeatureData(features);
    }

    @Override
    public BufferedImage getSource() {
        return source;
    }

}
