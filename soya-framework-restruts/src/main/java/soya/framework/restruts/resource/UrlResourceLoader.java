package soya.framework.restruts.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.Resource;
import soya.framework.restruts.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class UrlResourceLoader implements ResourceLoader {

    @Override
    public URL getResource(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        URL u = getResource(url);
        if (URL.class.isAssignableFrom(type)) {
            return (T) u;

        } else {
            Resource resource = new UrlResource(u);
            if (Resource.class.isAssignableFrom(type)) {
                return (T) resource;

            } else if (File.class.isAssignableFrom(type)) {
                try {
                    return (T) Paths.get(u.toURI()).toFile();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

            } else if (String.class.isAssignableFrom(type)) {
                try {
                    return (T) new UrlResource(new URL(url)).getAsString(Charset.defaultCharset());

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new Resource.ResourceException();
            }
        }
    }

    static class UrlResource implements Resource {
        private final URL url;

        public UrlResource(URL url) {
            this.url = url;
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                return IOUtils.toString(url, encoding);
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }
    }
}
