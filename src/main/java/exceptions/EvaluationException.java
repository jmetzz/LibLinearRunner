package exceptions;

/**
 * Created by Jean Metz.
 */
public class EvaluationException extends Throwable {

    public EvaluationException(String msg, Exception e) {
        super(msg, e);
    }
}
