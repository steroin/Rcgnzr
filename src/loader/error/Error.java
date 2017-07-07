package loader.error;

/**
 * Created by Sergiusz on 07.07.2017.
 */
public class Error {
    private String message;
    private ErrorType type;

    public Error(String msg, ErrorType errType) {
        message = msg;
        type = errType;
    }

    public Error(String msg) {
        this(msg, ErrorType.MINOR);
    }

    public Error() {
        this("Unknown error occurred");
    }

    public String getMessage() {
        return message;
    }

    public boolean isCritical() {
        return type == ErrorType.CRITICAL;
    }

    public boolean isImportant() {
        return type == ErrorType.IMPORTANT;
    }

    public boolean isMinor() {
        return type == ErrorType.MINOR;
    }
}
