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

    public void append(StringBuilder builder, int indent) {
        appendComments(builder, indent);
        builder.append(INDENTS[indent])
                .append(type)
                .append(" ")
                .append(statement)
                .append("\n");

        steps.forEach(a -> {
            a.append(builder, indent);
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 2);
        return builder.toString();
    }

    void add(StepDefinition step) {
        steps.add(step);
    }

    enum TYPE {
        Given, When, Then
    }
}
