package soya.framework.action;

public class ActionException extends Exception {

    protected ActionException() {
    }

    public ActionException(Throwable cause) {
        super(cause);
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

}
