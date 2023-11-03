package soya.framework.restruts;

public interface ResourceLoader {

    String[] getNamespaces();

    Object getWiredResource(String url);

    <T> T getWiredResource(String url, Class<T> type);

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
