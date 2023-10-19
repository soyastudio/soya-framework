package soya.framework.action;

import java.io.InputStream;
import java.nio.charset.Charset;

public interface Resource {

    String getAsString(Charset encoding) throws ResourceException;

    InputStream getAsInputStream() throws ResourceException;

}
