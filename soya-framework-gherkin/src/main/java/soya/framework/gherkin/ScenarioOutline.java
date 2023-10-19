package soya.framework.gherkin;

import java.util.List;

public class ScenarioOutline extends GherkinSyntaxNode {
    private Examples examples;
    protected ScenarioOutline(String statement, List<String> comments) {
        super(statement, comments);
    }

    public static ScenarioOutlineBuilder builder(String statement, List<String> comments) {
        return new ScenarioOutlineBuilder(statement, comments);
    }

    public static class ScenarioOutlineBuilder extends NodeBuilder<ScenarioOutlineBuilder> {
        private ScenarioOutline scenarioOutline;
        private ScenarioOutlineBuilder(String statement, List<String> comments) {
            this.scenarioOutline = new ScenarioOutline(statement, comments);
        }


        public ScenarioOutline create() {
            return scenarioOutline;
        }
    }
}
