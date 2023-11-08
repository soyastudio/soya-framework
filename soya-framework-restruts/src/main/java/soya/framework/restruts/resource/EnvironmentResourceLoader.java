package soya.framework.restruts.resource;

import soya.framework.restruts.NamespaceResourceLoader;
import soya.framework.restruts.RestActionContext;
import soya.framework.restruts.RestActionContextAware;
import soya.framework.restruts.util.ConvertUtils;

public class EnvironmentResourceLoader implements NamespaceResourceLoader, RestActionContextAware {
    private static final String ENVIRONMENT = "env:";
    private static final String[] NAMESPACES = {ENVIRONMENT};

    private RestActionContext context;

    @Override
    public void setContext(RestActionContext context) {
        this.context = context;
    }

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public String getResource(String url) {
        String propName = url.substring(ENVIRONMENT.length());
        return context.getProperty(propName);
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        return (T) ConvertUtils.convert(getResource(url), type);
    }
}
