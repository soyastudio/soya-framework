package soya.framework.action.flow;

import soya.framework.action.ActionName;

public interface Session {
    ActionName getActionName();

    String getId();

}
