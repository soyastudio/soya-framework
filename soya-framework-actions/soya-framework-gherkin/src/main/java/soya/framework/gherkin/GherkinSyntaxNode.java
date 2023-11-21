package soya.framework.gherkin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class GherkinSyntaxNode implements GherkinKeywords, Serializable {

    protected String statement;
    protected List<String> comments = new ArrayList<>();
    protected Map<String, String> annotations = new LinkedHashMap<>();

    protected GherkinSyntaxNode() {
    }

    protected GherkinSyntaxNode(List<String> comments) {
        this(null, comments);
    }

    protected GherkinSyntaxNode(String statement, List<String> comments) {
        this.statement = statement;
        comments.forEach(c -> {
            String ln = c.trim();
            if (ln.startsWith("@")) {
                int index = ln.indexOf("=");
                if (index > 0) {
                    String key = ln.substring(1, index);
                    String value = ln.substring(index + 1).trim();
                    annotations.put(key, value);
                } else {
                    this.comments.add(c);
                }

            } else {
                this.comments.add(c);
            }
        });
    }

    public String[] getComments() {
        return comments.toArray(new String[comments.size()]);
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    protected StringBuilder appendComments(StringBuilder builder, int indent) {
        comments.forEach(c -> {
            builder.append(INDENTS[indent])
                    .append(COMMENT)
                    .append(" ")
                    .append(c)
                    .append("\n");
        });

        annotations.entrySet().forEach(e -> {

            builder.append(INDENTS[indent])
                    .append(COMMENT)
                    .append(" ")
                    .append("@")
                    .append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("\n");
        });

        return builder;

    }


    protected static String toName(String statement) {
        return statement;
    }

    public static abstract class GherkinNodeBuilder<T extends GherkinNodeBuilder> {
        protected List<String> commentBuffer = new ArrayList<>();

        protected GherkinNodeBuilder() {
        }

        public T addComment(String line) {
            commentBuffer.add(line);
            return (T) this;
        }

        public abstract GherkinSyntaxNode create();
    }

}
