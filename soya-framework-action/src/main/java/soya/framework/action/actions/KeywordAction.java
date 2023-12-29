package soya.framework.action.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.dsl.Dictionary;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "dsl",
        name = "keyword"
)
public class KeywordAction implements Callable<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    private String word;

    @Override
    public String call() throws Exception {
        return Dictionary.getKeyword(word);
    }
}
