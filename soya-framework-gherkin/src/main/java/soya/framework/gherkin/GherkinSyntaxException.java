package soya.framework.gherkin;

public class GherkinSyntaxException extends RuntimeException {
    public GherkinSyntaxException() {
    }

    public GherkinSyntaxException(String message) {
        super(message);
    }

    public GherkinSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public GherkinSyntaxException(Throwable cause) {
        super(cause);
    }
}
