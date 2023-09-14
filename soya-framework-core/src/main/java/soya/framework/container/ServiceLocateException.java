package soya.framework.container;

public class ServiceLocateException extends RuntimeException {

    public ServiceLocateException(String message) {
        super(message);
    }

    public ServiceLocateException(String message, Throwable cause) {
        super(message, cause);
    }
}
