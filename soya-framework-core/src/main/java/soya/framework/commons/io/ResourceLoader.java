package soya.framework.commons.io;

import java.net.URI;

public interface ResourceLoader {
    Resource load(URI uri) throws ResourceException;
}
