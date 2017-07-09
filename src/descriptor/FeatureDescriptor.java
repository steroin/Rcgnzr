package descriptor;

import loader.Image;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public interface FeatureDescriptor {
    FeatureData describe();
    Image getSource();
    void loadSource(Image src);
}
