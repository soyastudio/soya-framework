package soya.framework.action.rest;

import soya.framework.restruts.ActionMapping;
import soya.framework.restruts.RestActionLoader;

import java.util.Set;

public class PipelineRestAdapter implements RestActionLoader {

    private NamingStrategy namingStrategy = new ActionRestAdapter.DefaultNamingStrategy();

    @Override
    public Set<ActionMapping> load() {
        return null;
    }
}
