package soya.framework.gherkin;

import java.util.List;

public class Scenario extends GherkinSyntaxNode {
    private String name;
    private StepHolder given;
    private StepHolder when;
    private StepHolder then;

    protected Scenario(String statement, List<String> comments) {
        super(statement, comments);
        this.name = annotations.containsKey("name") ? annotations.get("name") : toName(statement);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        System.out.println("============= given: " + given);
        System.out.println("============= when: " + when);
        System.out.println("============= then: " + then);

        StringBuilder builder = new StringBuilder();
        comments.forEach(c -> {
            builder.append(INDENT)
                    .append(COMMENT)
                    .append(" ")
                    .append(c)
                    .append("\n");
        });

        annotations.entrySet().forEach(e -> {

            builder.append(INDENT)
                    .append(COMMENT)
                    .append(" ")
                    .append("@")
                    .append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("\n");
        });

        builder.append(INDENT).append(SCENARIO).append(statement).append("\n");

        if(given != null) {
            builder.append(given);
        }

        if(when != null) {
            builder.append(when);
        }

        if(then != null) {
            builder.append(then);
        }

        return builder.toString();
    }

    public static ScenarioBuilder builder(String statement, List<String> comments) {
        return new ScenarioBuilder(statement, comments);
    }

    public static class ScenarioBuilder extends NodeBuilder<ScenarioBuilder> {

        private Scenario scenario;
        private StepHolder stepHolder;

        private ScenarioBuilder(String statement, List<String> comments) {
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
