package soya.framework.action;

import java.util.concurrent.Callable;

public interface ActionCallable extends Callable<ActionResult> {

    ActionName actionName();

    @Override
    ActionResult call();
}
