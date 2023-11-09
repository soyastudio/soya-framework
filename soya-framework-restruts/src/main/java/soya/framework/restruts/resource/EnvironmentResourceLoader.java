package soya.framework.restruts.resource;

import soya.framework.restruts.*;
import soya.framework.restruts.util.ConvertUtils;

public class EnvironmentResourceLoader implements NamespaceAware, RestActionContextAware, ResourceLoader {
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
    public Resource load(String location) throws ResourceException {
        return null;
    }


}
