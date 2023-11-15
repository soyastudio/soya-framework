package soya.framework.context;

public class ServiceLocateException extends RuntimeException {
    public ServiceLocateException() {
    }

    public ServiceLocateException(String message) {
        super(message);
    }

    public ServiceLocateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLocateException(Throwable cause) {
        super(cause);
    }
}
