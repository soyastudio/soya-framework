package soya.framework.commons.io;

import java.net.URI;
import java.nio.charset.Charset;

public interface Resource<T> {
    URI getURI();

    T get() throws ResourceException;

    String getAsString(Charset encoding) throws ResourceException;
}
