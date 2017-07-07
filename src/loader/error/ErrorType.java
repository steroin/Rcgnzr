package loader.error;

/**
 * Created by Sergiusz on 07.07.2017.
 */
public enum ErrorType {
    MINOR,      //doesnt affect primary functionalities
    IMPORTANT,  //affects primary functionalities, but doesnt stop workflow
    CRITICAL    //stops workflow of application
}
