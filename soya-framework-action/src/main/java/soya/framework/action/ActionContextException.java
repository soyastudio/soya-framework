package soya.framework.action;

public class ActionContextException extends RuntimeException {

    public ActionContextException() {
    }

    public ActionContextException(String message) {
        super(message);
    }

    public ActionContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionContextException(Throwable cause) {
        super(cause);
    }

}
