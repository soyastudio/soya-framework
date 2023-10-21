package soya.framework.gherkin;

import java.util.List;

public class Scenario extends GherkinSyntaxNode {
    protected String name;
    protected StepHolder given;
    protected StepHolder when;
    protected StepHolder then;

    public Scenario() {
        super();
    }

    protected Scenario(String statement, List<String> comments) {
        super(statement, comments);
        this.name = annotations.containsKey("name") ? annotations.get("name") : toName(statement);
    }

    public String getName() {
        return name;
    }

    public void append(StringBuilder builder, int indent) {

        appendComments(builder, indent);

        builder.append(INDENTS[indent]).append(SCENARIO).append(statement).append("\n");

        if(given != null) {
            given.append(builder, 2);
        }

        if(when != null) {
            when.append(builder, 2);
        }

        if(then != null) {
            then.append(builder, 2);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, 1);
        return builder.toString();
    }

    public static ScenarioBuilder builder(String statement, List<String> comments) {
        return new ScenarioBuilder(statement, comments);
    }

    public static class ScenarioBuilder extends GherkinNodeBuilder<ScenarioBuilder> {

        private Scenario scenario;
        private StepHolder stepHolder;

        protected ScenarioBuilder(String statement, List<String> comments) {
            super();
            this.scenario = new Scenario(statement, comments);
        }

        public ScenarioBuilder given(String line) {
            if (stepHolder == null) {
                this.stepHolder = new StepHolder(line, commentBuffer, StepHolder.TYPE.Given);
                scenario.given = stepHolder;

            } else if (stepHolder.getType().equals(StepHolder.TYPE.Given)) {
                this.stepHolder.add(new StepDefinition(line, commentBuffer));

            } else {
                throw new GherkinSyntaxException();
            }

            commentBuffer.clear();
            return this;
        }

        public ScenarioBuilder when(String line) {
            if (stepHolder == null || stepHolder.getType().equals(StepHolder.TYPE.Given)) {
                this.stepHolder = new StepHolder(line, commentBuffer, StepHolder.TYPE.When);
                scenario.when = stepHolder;

            } else if (stepHolder.getType().equals(StepHolder.TYPE.When)) {
                this.stepHolder.add(new StepDefinition(line, commentBuffer));

            } else if ((stepHolder.getType().equals(StepHolder.TYPE.Then))) {
                System.out.println("------------------- todo");
            }

            commentBuffer.clear();
            return this;
        }

        public ScenarioBuilder then(String line) {
            if (stepHolder == null) {
                throw new GherkinSyntaxException();

            } else if (stepHolder.getType().equals(StepHolder.TYPE.Then)) {
                this.stepHolder.add(new StepDefinition(line, commentBuffer));

            } else {
                this.stepHolder = new StepHolder(line, commentBuffer, StepHolder.TYPE.Then);
                scenario.then = stepHolder;

            }

            commentBuffer.clear();
            return this;
        }

        public ScenarioBuilder and(String line) {
            if (stepHolder == null) {
                throw new GherkinSyntaxException("");
            }

            this.stepHolder.add(new StepDefinition(line, commentBuffer));
            commentBuffer.clear();
            return this;

        }

        public Scenario create() {
            return scenario;
        }
    }
}
