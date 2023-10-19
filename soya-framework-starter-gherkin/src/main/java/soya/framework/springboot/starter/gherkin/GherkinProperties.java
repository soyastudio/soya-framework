package soya.framework.springboot.starter.gherkin;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "soya.framework.gherkin")
public class GherkinProperties {
    public static final String DEFAULT_PATH = "/gherkin/*";

    private String path = DEFAULT_PATH;

    public String getPath() {
        return path;
    }
}
