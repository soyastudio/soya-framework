package soya.framework.action.dsl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.ActionName;

public class ActionParser {

    public static ActionName parse(String exp) throws IllegalArgumentException {

        ActionSyntaxTree tree = ActionSyntaxTree.parse(exp);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(tree));

        return ActionName.fromURI("test://test");
    }
}
