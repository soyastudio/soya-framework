package soya.framework.commons.io.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class URLResourceLoader implements ResourceLoader {
    @Override
    public Resource load(String url) throws ResourceException {
        try {
            return new URLResource(new URL(url));

        } catch (MalformedURLException e) {
            throw new ResourceException(e);
        }
    }

    static class URLResource implements Resource {
        private final URL url;

        URLResource(URL url) {
            this.url = url;
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new ResourceException(e);
            }
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
