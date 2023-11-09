package soya.framework.restruts;

import java.io.InputStream;
import java.nio.charset.Charset;

public interface Resource {
    InputStream getAsInputStream() throws ResourceException;

    String getAsString(Charset encoding) throws ResourceException;
}
