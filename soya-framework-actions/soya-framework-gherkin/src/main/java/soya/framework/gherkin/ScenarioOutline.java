package soya.framework.gherkin;

import java.util.List;

public class ScenarioOutline extends Scenario {
    private Examples examples;

    protected ScenarioOutline(Scenario scenario, Examples examples) {
        super();
        this.name = scenario.name;
        this.statement = scenario.statement;
        this.comments = scenario.comments;
        this.annotations = scenario.annotations;
        this.given = scenario.given;
        this.when = scenario.when;
        this.then = scenario.then;

        this.examples = examples;
    }

    public void append(StringBuilder builder, int indent) {
        appendComments(builder, indent);
        builder.append(INDENTS[indent]).append(SCENARIO_OUTLINE).append(statement).append("\n");

        int next = indent + 1;
        if(given != null) {
            given.append(builder, next);
        }

        if(when != null) {
            when.append(builder, next);
        }

        if(then != null) {
            then.append(builder, next);
        }

        examples.append(builder, next);

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 1);
        return builder.toString();
    }

    public static ScenarioOutlineBuilder builder(String statement, List<String> comments) {
        return new ScenarioOutlineBuilder(statement, comments);
    }

    public static class ScenarioOutlineBuilder extends ScenarioBuilder {
        private Examples examples;

        protected ScenarioOutlineBuilder(String statement, List<String> comments) {
            super(statement, comments);

        }

        @Override
        public ScenarioOutlineBuilder given(String line) {
            super.given(line);
            return this;
        }

        @Override
        public ScenarioOutlineBuilder when(String line) {
            super.when(line);
            return this;
        }

        @Override
        public ScenarioOutlineBuilder then(String line) {
            super.then(line);

            return this;
        }

        @Override
        public ScenarioOutlineBuilder and(String line) {
            super.and(line);

            return this;
        }

        public ScenarioOutlineBuilder examples() {
            this.examples = new Examples(commentBuffer);
            commentBuffer.clear();
            return this;
        }

        public ScenarioOutlineBuilder appendDataLine(String line) {
            if(examples != null) {
                examples.addDataLine(line);
            }
            return this;
        }

        public ScenarioOutline create() {
            return new ScenarioOutline(super.create(), examples);
        }
    }
}
