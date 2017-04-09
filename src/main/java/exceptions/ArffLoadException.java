package exceptions;

/**
 * Created by Jean Metz.
 */
public class ArffLoadException extends Throwable {

    public ArffLoadException(String msg, Exception e) {
        super(msg, e);
    }
}
