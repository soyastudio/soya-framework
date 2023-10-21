package soya.framework.gherkin;

import java.util.ArrayList;
import java.util.List;

public class Examples extends GherkinSyntaxNode {
    private List<String> dataLines = new ArrayList<>();
    public Examples(List<String> comments) {
        super(comments);
    }

    public void addDataLine(String ln) {
        dataLines.add(ln);
    }

    public void append(StringBuilder builder, int indent) {
        builder.append("\n");

        builder.append(INDENTS[indent]).append(EXAMPLES).append("\n");
        dataLines.forEach(e -> {
            builder.append(INDENTS[indent + 1]).append(e).append("\n");
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 2);
        return builder.toString();
    }
}
