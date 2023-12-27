package soya.framework.action.dsl.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.dsl.Dictionary;

import java.util.concurrent.Callable;
@ActionDefinition(
        domain = "dsl",
        name = "keywords"
)
public class KeywordsAction implements Callable<String[]> {

    @Override
    public String[] call() throws Exception {
        return Dictionary.getKeywords();
    }
}
