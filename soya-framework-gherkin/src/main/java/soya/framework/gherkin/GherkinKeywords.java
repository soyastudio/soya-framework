package soya.framework.gherkin;

public interface GherkinKeywords {

    public static final String FEATURE = "Feature:";

    public static final String BACKGROUND = "Background:";

    public static final String SCENARIO_OUTLINE = "Scenario Outline:";

    public static final String RULE = "Rule";

    public static final String SCENARIO = "Scenario:";

    public static final String GIVEN = "Given ";

    public static final String WHEN = "When ";

    public static final String THEN = "Then ";

    public static final String AND = "And ";

    public static final String BUT = "But ";

    public static final String EXAMPLE = "Example: ";

    public static final String EXAMPLES = "Examples:";

    public static final String COMMENT = "#";

    public static final String TABLE_SEPARATOR = "|";

    public static final String INDENT = "  ";

    public static final String[] INDENTS = {
            "",
            INDENT + INDENT,
            INDENT + INDENT + INDENT,
            INDENT + INDENT + INDENT + INDENT,
            INDENT + INDENT + INDENT + INDENT + INDENT,
            INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
    };
}
