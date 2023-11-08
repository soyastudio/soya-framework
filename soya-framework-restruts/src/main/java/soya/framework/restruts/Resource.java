package soya.framework.restruts;

import java.io.InputStream;
import java.nio.charset.Charset;

public interface Resource {
    String getAsString(Charset encoding) throws ResourceException;

    InputStream getAsInputStream() throws ResourceException;

    class ResourceException extends RuntimeException {

        public ResourceException() {
        }

        public ResourceException(String message) {
            super(message);
        }

        public ResourceException(String message, Throwable cause) {
            super(message, cause);
        }

        public ResourceException(Throwable cause) {
            super(cause);
        }
    }

}
