package descriptor;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class FeatureData {
    private double[] vector;

    public FeatureData(double[] vec) {
         this.vector = vec;
    }

    public double[] getAsVector() {
        return vector;
    }
}
