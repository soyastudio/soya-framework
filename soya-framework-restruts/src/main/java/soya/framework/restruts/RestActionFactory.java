package soya.framework.restruts;

import java.util.concurrent.Callable;

public interface RestActionFactory {
    String getNamespace();
    Callable<?> create(ActionMapping mapping) throws ActionCreationException;

    class ActionCreationException extends RuntimeException {
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
}
