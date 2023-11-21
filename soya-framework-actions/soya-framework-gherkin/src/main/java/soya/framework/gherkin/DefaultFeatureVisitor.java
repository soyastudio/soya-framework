package soya.framework.gherkin;

import java.util.ArrayList;
import java.util.List;

public class DefaultFeatureVisitor extends FeatureVisitor<Feature> {

    private List<String> commentBuffer = new ArrayList<>();

    private Feature.FeatureBuilder featureBuilder;

    private GherkinSyntaxNode.GherkinNodeBuilder nodeBuilder;

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
    protected void visitBackground() {
        if (featureBuilder == null) {
            throw new GherkinSyntaxException();
        }

        this.nodeBuilder = Background.builder(commentBuffer);
        commentBuffer.clear();

    }

    @Override
    protected void visitScenarioOutline(String line) {
        if (nodeBuilder != null) {
            visitEnd();
        }

        nodeBuilder = ScenarioOutline.builder(line, commentBuffer);
        commentBuffer.clear();
    }

    @Override
    protected void visitScenario(String line) {
        if (nodeBuilder != null) {
            visitEnd();
        }

        nodeBuilder = Scenario.builder(line, commentBuffer);
        commentBuffer.clear();
    }

    @Override
    protected void visitGiven(String line) {
        if (nodeBuilder instanceof Background.BackgroundBuilder) {
            Background.BackgroundBuilder backgroundBuilder = (Background.BackgroundBuilder) nodeBuilder;
            backgroundBuilder.given(line);

        } else if (nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.given(line);
        }

        commentBuffer.clear();

    }

    @Override
    protected void visitWhen(String line) {
        if (nodeBuilder instanceof Background.BackgroundBuilder) {

        } else if (nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.when(line);
        }

        commentBuffer.clear();
    }

    @Override
    protected void visitThen(String line) {
        if (nodeBuilder instanceof Background.BackgroundBuilder) {

        } else if (nodeBuilder instanceof Scenario.ScenarioBuilder) {
            Scenario.ScenarioBuilder builder = (Scenario.ScenarioBuilder) nodeBuilder;
            builder.then(line);
        }

        commentBuffer.clear();
    }

    @Override
    protected void visitAnd(String line) {
        if (nodeBuilder instanceof Background.BackgroundBuilder) {
            Background.BackgroundBuilder backgroundBuilder = (Background.BackgroundBuilder) nodeBuilder;
            backgroundBuilder.and(line);

        }  else  if (nodeBuilder instanceof Scenario.ScenarioBuilder) {
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
    protected void visitExamples() {
        ScenarioOutline.ScenarioOutlineBuilder builder = (ScenarioOutline.ScenarioOutlineBuilder) nodeBuilder;
        builder.examples();
        commentBuffer.clear();
    }

    @Override
    protected void visitDataLine(String line) {
        ScenarioOutline.ScenarioOutlineBuilder builder = (ScenarioOutline.ScenarioOutlineBuilder) nodeBuilder;
        builder.appendDataLine(line);
    }

    @Override
    protected void visitTextLine(String line) {
        if (nodeBuilder != null) {

        } else if (featureBuilder != null) {
            featureBuilder.addDescription(line);
        }

    }

    @Override
    protected void visitComment(String line) {
        if (nodeBuilder != null) {
            nodeBuilder.addComment(line);
        } else {
            commentBuffer.add(line);

        }
    }

    @Override
    protected void visitEnd() {
        if (nodeBuilder != null) {
            commentBuffer.addAll(nodeBuilder.commentBuffer);

            if (nodeBuilder instanceof Background.BackgroundBuilder) {
                Background.BackgroundBuilder backgroundBuilder = (Background.BackgroundBuilder) nodeBuilder;
                featureBuilder.addBackground(backgroundBuilder.create());

            } else if (nodeBuilder instanceof Scenario.ScenarioBuilder) {
                Scenario.ScenarioBuilder scenarioBuilder = (Scenario.ScenarioBuilder) nodeBuilder;
                featureBuilder.addScenario(scenarioBuilder.create());
            }
        }
    }
}
