package soya.framework.action.dsl;

import soya.framework.action.ActionName;

public class ActionParser {

    public static ActionName parse(String exp) throws IllegalArgumentException {

        return ActionName.fromURI("test://test");
    }
}
