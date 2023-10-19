package soya.framework.gherkin;

import java.util.ArrayList;
import java.util.List;

public class StepHolder extends GherkinSyntaxNode {
    private final TYPE type;
    private List<StepDefinition> steps = new ArrayList<>();

    public StepHolder(String statement, List<String> comments, TYPE type) {
        super(statement, comments);
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }

    public StepDefinition[] getSteps() {
        return steps.toArray(new StepDefinition[steps.size()]);
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
                .append(type)
                .append(" ")
                .append(statement)
                .append("\n");

        steps.forEach(a -> {
            builder.append(a);
        });

        return builder.toString();
    }

    void add(StepDefinition step) {
        steps.add(step);
    }

    enum TYPE {
        Given, When, Then
    }
}
