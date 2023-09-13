package soya.framework.action;

import java.io.Serializable;

public interface ActionResult extends Serializable {

    ActionName actionName();

    Object get();

    boolean success();

}
