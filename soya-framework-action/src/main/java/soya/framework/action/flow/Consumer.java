package soya.framework.action.flow;

import java.util.Map;

public interface Consumer<T> {
    Map<String, Object> consume(T input, Pipeline pipeline);
}
