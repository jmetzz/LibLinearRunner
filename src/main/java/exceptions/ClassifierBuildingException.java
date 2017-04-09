package exceptions;

/**
 * Created by Jean Metz.
 */
public class ClassifierBuildingException extends Exception {
    public ClassifierBuildingException(String msg, Exception e) {
        super(msg, e);
    }

}
