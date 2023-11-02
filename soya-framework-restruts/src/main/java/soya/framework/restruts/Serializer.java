package soya.framework.restruts;

public interface Serializer {
    <T> byte[] serialize(T o) throws SerializationException;

    <T> T deserialize(byte[] data, Class<T> type) throws SerializationException;

    class SerializationException extends RuntimeException {
        public SerializationException() {
        }

        public SerializationException(String message) {
            super(message);
        }

        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }

        public SerializationException(Throwable cause) {
            super(cause);
        }
    }
}
