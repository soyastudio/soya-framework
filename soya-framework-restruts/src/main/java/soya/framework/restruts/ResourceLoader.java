package soya.framework.restruts;

public interface ResourceLoader {

    Object getResource(String url);

    <T> T getResource(String url, Class<T> type);

    class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException() {
        }

        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ResourceNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
