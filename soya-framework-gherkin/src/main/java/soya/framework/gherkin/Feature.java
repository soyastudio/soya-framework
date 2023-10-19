package soya.framework.gherkin;

import java.util.*;

public class Feature extends GherkinSyntaxNode {
    private String name;
    private List<String> descriptions = new ArrayList<>();
    private Map<String, Scenario> scenarios = new LinkedHashMap<>();

    protected Feature(String statement, List<String> comments) {
        super(statement, comments);
        this.name = annotations.containsKey("name") ? annotations.get("name") : toName(statement);
    }

    public String getName() {
        return name;
    }

    public String[] getDescriptions() {
        return descriptions.toArray(new String[descriptions.size()]);
    }

    public String toString() {

        StringBuilder builder = new StringBuilder();
        comments.forEach(c -> {
            builder.append(COMMENT)
                    .append(" ")
                    .append(c)
                    .append("\n");
        });

        annotations.entrySet().forEach(e -> {

            builder.append(COMMENT)
                    .append(" ")
                    .append("@")
                    .append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("\n");
        });

        builder.append(FEATURE).append(statement).append("\n");

        descriptions.forEach(d -> {
            builder.append(INDENT).append(d).append("\n");
        });

        scenarios.values().forEach(s -> {
            builder.append(s).append("\n");
        });

        return builder.toString();
    }

    public static FeatureBuilder builder(String statement, List<String> comments) {
        return new FeatureBuilder(statement, comments);
    }

    static class FeatureBuilder {
        private Feature feature;
        private List<String> commentBuffer = new ArrayList<>();

        private FeatureBuilder(String statement, List<String> comments) {
            this.feature = new Feature(statement, comments);
        }

        public FeatureBuilder addDescription(String line) {
            feature.descriptions.add(line);
            return this;
        }

        public FeatureBuilder addScenario(Scenario scenario) {
            feature.scenarios.put(scenario.getName(), scenario);
            return this;
        }

        public Feature create() {
            return feature;
        }
    }
}
