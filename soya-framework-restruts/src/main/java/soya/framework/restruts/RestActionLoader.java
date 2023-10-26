package soya.framework.restruts;

import java.util.Set;

public interface RestActionLoader<T> {
    Set<RestActionRegistry> load(T resource);
}
