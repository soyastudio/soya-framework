package soya.framework.gherkin;

import java.util.ArrayList;
import java.util.List;

public class DefaultFeatureVisitor extends FeatureVisitor<Feature> {

    private List<String> commentBuffer = new ArrayList<>();

    private Feature.FeatureBuilder featureBuilder;

    private GherkinSyntaxNode.NodeBuilder nodeBuilder;

    public DefaultFeatureVisitor() {
    }

    @Override
    protected Feature getResult() {
        return featureBuilder.create();
    }

    @Override
    protected void visitFeature(String line) {
        this.featureBuilder = Feature.builder(line, commentBuffer);
        commentBuffer.clear();
    }

    @Override
    protected void visitScenario(String line) {
        if(nodeBuilder != null) {
            visitEnd();
        }

        nodeBuilder = Scenario.builder(line, commentBuffer);
        commentBuffer.clear();
    }

    @Override
    protected void visitGiven(String line) {
        if(nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.given(line);
        }

        commentBuffer.clear();

    }

    @Override
    protected void visitWhen(String line) {
        if(nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.when(line);
        }

        commentBuffer.clear();
    }

    @Override
    protected void visitThen(String line) {
        if(nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.then(line);
        }

        commentBuffer.clear();
    }

    @Override
    protected void visitAnd(String line) {
        if(nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.and(line);
        }

        commentBuffer.clear();
    }

    @Override
    protected void visitExample(String line) {

        commentBuffer.clear();
    }

    @Override
    protected void visitExamples(String line) {

        commentBuffer.clear();
    }

    @Override
    protected void visitTextLine(String line) {
        if(nodeBuilder != null) {

        } else if(featureBuilder != null) {
            featureBuilder.addDescription(line);
        }

    }

    @Override
    protected void visitComment(String line) {
        if(nodeBuilder != null) {
            nodeBuilder.addComment(line);
        } else {
            commentBuffer.add(line);

        }
    }

    @Override
    protected void visitEnd() {
        if(nodeBuilder != null) {
            commentBuffer.addAll(nodeBuilder.commentBuffer);

            if(nodeBuilder instanceof Scenario.ScenarioBuilder) {
                Scenario.ScenarioBuilder scenarioBuilder = (Scenario.ScenarioBuilder) nodeBuilder;
                featureBuilder.addScenario(scenarioBuilder.create());
            }
        }
    }
}
