package soya.framework.action.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ActionSyntaxTree {

    private ActionPhrase activity;
    private List<ActionPhrase> prepositionalPhrases = new ArrayList<>();
    private String complements;

    public static ActionSyntaxTree parse(String exp) throws SyntaxException {
        ActionSyntaxTreeBuilder builder = new ActionSyntaxTreeBuilder();
        StringTokenizer tokenizer = new StringTokenizer(exp);
        while (tokenizer.hasMoreTokens()) {
            builder.accept(tokenizer.nextToken());
        }

        return builder.create();
    }

    static class ActionSyntaxTreeBuilder {

        private ActionSyntaxTree tree;
        private ActionPhrase phrase;

        private ActionSyntaxTreeBuilder() {
            this.tree = new ActionSyntaxTree();
        }

        public ActionSyntaxTreeBuilder accept(String word) {
            String keyword = Dictionary.getKeyword(word);
            if (keyword != null) {
                if (phrase != null) {
                    if (Dictionary.isActivity(phrase.getKeyword())) {
                        tree.activity = phrase;
                    } else {
                        tree.prepositionalPhrases.add(phrase);
                    }
                }

                this.phrase = new ActionPhrase(keyword);

            } else {
                if (phrase != null) {
                    phrase.getObjects().add(word);

                } else {
                    throw new SyntaxException();

                }
            }

            return this;
        }

        ActionSyntaxTree create() {
            if (phrase != null) {
                if (Dictionary.isActivity(phrase.getKeyword())) {
                    tree.activity = phrase;
                } else {
                    tree.prepositionalPhrases.add(phrase);
                }
            } else {
                throw new IllegalStateException("");
            }

            return tree;
        }
    }
}
