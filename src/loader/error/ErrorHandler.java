package loader.error;

import java.util.ArrayList;

/**
 * Created by Sergiusz on 07.07.2017.
 */
public class ErrorHandler {
    private ArrayList<Error> errorList;

    public ErrorHandler() {
        errorList = new ArrayList<>();
    }

    public void handle(Error err) {
        errorList.add(err);
    }

    public void clearErrorList() {
        errorList.clear();
    }
}
