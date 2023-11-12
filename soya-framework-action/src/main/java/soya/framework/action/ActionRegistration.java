package soya.framework.action;

import java.util.concurrent.Callable;

public interface ActionRegistration {

    String[] domains();

    ActionName[] actions(String domain);

    Class<? extends Callable>  actionType(ActionName actionName);
}
