package soya.framework.action.dsl;

import java.util.ArrayList;
import java.util.List;

public class ActionSyntaxTree {

    private ActionPhrase activity;

    private List<ActionPhrase> prepositionalPhrases = new ArrayList<>();

    private String complements;

    public static ActionSyntaxTreeBuilder builder() {
        return new ActionSyntaxTreeBuilder();
    }

    public static class ActionSyntaxTreeBuilder {

        private ActionSyntaxTreeBuilder() {
        }

        public ActionSyntaxTreeBuilder accept(String word) {
            return this;
        }

        public ActionSyntaxTree create() {
            return new ActionSyntaxTree();
        }
    }
}
