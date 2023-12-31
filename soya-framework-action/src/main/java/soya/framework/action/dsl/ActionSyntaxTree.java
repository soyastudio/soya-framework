package soya.framework.action.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ActionSyntaxTree {

    private ActionPhrase domain;
    private ActionPhrase activity;
    private List<ActionPhrase> prepositionalPhrases = new ArrayList<>();
    private List<ActionPhrase> complements = new ArrayList<>();

    public static ActionSyntaxTree parse(String exp) throws SyntaxException {
        ActionSyntaxTreeBuilder builder = new ActionSyntaxTreeBuilder();
        StringTokenizer tokenizer = new StringTokenizer(exp);
        while (tokenizer.hasMoreTokens()) {
            builder.accept(tokenizer.nextToken());
        }

        return builder.create();
    }

    void addPhrase(ActionPhrase phrase) {
        String keyword = phrase.getKeyword();
        if ("WITHIN".equalsIgnoreCase(keyword)
                || "IN".equalsIgnoreCase(keyword)
                || "UNDER".equalsIgnoreCase(keyword)) {
            domain = phrase;

        } else if (Dictionary.isActivity(phrase.getKeyword())) {
            activity = phrase;

        } else if (Dictionary.isKeyword(phrase.getKeyword())) {
            prepositionalPhrases.add(phrase);

        } else {
            complements.add(phrase);
        }
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
                    tree.addPhrase(phrase);
                }

                this.phrase = new ActionPhrase(keyword);

            } else if (Dictionary.isProposition(word)) {
                if (phrase != null) {
                    tree.addPhrase(phrase);
                }

                this.phrase = new ActionPhrase(word.toUpperCase());

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
                tree.addPhrase(phrase);
            } else {
                throw new IllegalStateException("");
            }

            return tree;
        }
    }
}
