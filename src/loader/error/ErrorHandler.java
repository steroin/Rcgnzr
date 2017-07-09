package loader.error;

import java.util.ArrayList;

/**
 * Created by Sergiusz on 07.07.2017.
 */
public class ErrorHandler {
    private static ErrorHandler instance = null;
    private ArrayList<Error> errorList;

    private ErrorHandler() {
        errorList = new ArrayList<>();
    }

    public void handle(Error err) {
        errorList.add(err);
    }

    public void clearErrorList() {
        errorList.clear();
    }

    public static ErrorHandler getInstance() {
        if (instance == null) instance = new ErrorHandler();
        return instance;
    }
}
