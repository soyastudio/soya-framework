package soya.framework.gherkin;

import java.util.List;

public class StepDefinition extends GherkinSyntaxNode {

    public StepDefinition(String statement, List<String> comments) {
        super(statement, comments);
    }

    public void append(StringBuilder builder, int indent) {
        appendComments(builder, indent);
        builder.append(INDENTS[indent])
                .append(AND)
                .append(statement)
                .append("\n");
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 2);
        return builder.toString();
    }
}
