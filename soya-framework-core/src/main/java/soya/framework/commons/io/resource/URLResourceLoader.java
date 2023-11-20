package soya.framework.commons.io.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

public class URLResourceLoader implements ResourceLoader {
    @Override
    public Resource load(URI uri) throws ResourceException {
        return new URLResource(uri);
    }

    static class URLResource extends AbstractResource<URL> {
        private final URL url;

        protected URLResource(URI uri) {
            super(uri);
            try {
                this.url = uri.toURL();
            } catch (MalformedURLException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public URL get() throws ResourceException {
            return url;
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                return IOUtils.toString(url, encoding);

            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }
    }
}
