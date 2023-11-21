package soya.framework.gherkin;

import java.util.List;

public class Background extends GherkinSyntaxNode {
    private StepHolder given;

    protected Background(List<String> comments) {
        super(comments);
    }

    public void append(StringBuilder builder, int indent) {
        appendComments(builder, indent);
        builder.append(INDENTS[indent]).append(BACKGROUND).append("\n");
        if(given != null) {
            given.append(builder, indent + 1);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 1);
        return builder.toString();
    }

    public static BackgroundBuilder builder(List<String> comments) {
        return new BackgroundBuilder(comments);
    }

    public static class BackgroundBuilder extends GherkinNodeBuilder {

        private Background background;

        private BackgroundBuilder(List<String> comments) {
            super();
            this.background = new Background(comments);
        }

        public BackgroundBuilder given(String line) {
            background.given = new StepHolder(line, commentBuffer, StepHolder.TYPE.Given);
            commentBuffer.clear();
            return this;
        }
        public BackgroundBuilder and(String line) {
            if (background.given == null) {
                throw new GherkinSyntaxException("");
            }

            background.given.add(new StepDefinition(line, commentBuffer));
            commentBuffer.clear();
            return this;

        }

        @Override
        public Background create() {
            return background;
        }
    }
}
