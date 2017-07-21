package descriptor;

import java.awt.image.BufferedImage;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public interface FeatureDescriptor {
    FeatureDescriptionResult describe();
    void setSource(BufferedImage image);
}
