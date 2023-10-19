package soya.framework.gherkin;

import java.util.List;

public class StepDefinition extends GherkinSyntaxNode {

    public StepDefinition(String statement, List<String> comments) {
        super(statement, comments);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        comments.forEach(c -> {
            builder.append(INDENT).append(INDENT)
                    .append(COMMENT)
                    .append(" ")
                    .append(c)
                    .append("\n");
        });

        annotations.entrySet().forEach(e -> {

            builder.append(INDENT).append(INDENT)
                    .append(COMMENT)
                    .append(" ")
                    .append("@")
                    .append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("\n");
        });

        builder.append(INDENT).append(INDENT)
                .append(AND)
                .append(statement)
                .append("\n");

        return builder.toString();
    }
}
