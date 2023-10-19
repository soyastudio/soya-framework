package soya.framework.context;

import java.util.Set;

public interface ComponentScanner<T> {
    Set<T> scan(Class<?> containerType, String[] packages);
}
