package descriptor.hog;

/**
 * Created by Sergiusz on 09.07.2017.
 */
public class HOGSettings {
    private int cellWidth;
    private int cellHeight;
    private int blockWidth;
    private int blockHeight;
    private boolean signedGradients;
    public static final HOGSettings DEFAULT = new HOGSettings(8, 8, 2, 2, false);

    public HOGSettings(int cellWidth, int cellHeight, int blockWidth, int blockHeight, boolean signedGradients) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.signedGradients = signedGradients;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public boolean isSignedGradients() {
        return signedGradients;
    }
}
