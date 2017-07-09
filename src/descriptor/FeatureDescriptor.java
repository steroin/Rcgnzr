package descriptor;

import loader.Image;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public interface FeatureDescriptor {
    FeatureData describe();
    BufferedImage getSource();
}
