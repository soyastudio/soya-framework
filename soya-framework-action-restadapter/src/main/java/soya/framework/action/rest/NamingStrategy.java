package soya.framework.action.rest;

import soya.framework.action.ActionName;

public interface NamingStrategy {

    String toTag(ActionName actionName);

    String toPath(ActionName actionName);

    String toId(ActionName actionName);

}
