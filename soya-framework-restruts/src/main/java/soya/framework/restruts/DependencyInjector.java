package soya.framework.restruts;

public interface DependencyInjector {
    String[] getNamespaces();

    Object getWiredResource(String name);

    <T> T getWiredResource(String resource, Class<T> type);

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
