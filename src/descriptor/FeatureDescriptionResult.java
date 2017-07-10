package descriptor;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class FeatureDescriptionResult {
    private double[] vector;

    public FeatureDescriptionResult(double[] vec) {
         this.vector = vec;
    }

    public double[] getAsVector() {
        return vector;
    }
}
