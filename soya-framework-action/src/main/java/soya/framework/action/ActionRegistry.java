package soya.framework.action;

import java.util.Collection;

public interface ActionRegistry {
    String id();

    long lastUpdatedTime();

    Collection<ActionDomain> domains();

    Collection<ActionDescription> actions();

    ActionFactory actionFactory();

}
