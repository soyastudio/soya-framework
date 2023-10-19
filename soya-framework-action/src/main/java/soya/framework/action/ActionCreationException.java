package soya.framework.action;

public class ActionCreationException extends RuntimeException {

    public ActionCreationException() {

    }

    public ActionCreationException(String message) {
        super(message);
    }

    public ActionCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionCreationException(Throwable cause) {
        super(cause);
    }
}
