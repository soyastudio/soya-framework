package soya.framework.action.dsl;

import soya.framework.action.ActionName;

import java.util.StringTokenizer;

public class ActionParser {

    public static ActionName parse(String exp) throws IllegalArgumentException {
        StringTokenizer tokenizer = new StringTokenizer(exp);


        return ActionName.fromURI("test://test");
    }
}
