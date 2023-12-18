package soya.framework.action.orchestration;

public final class Mocking {
    private Mocking() {
    }

    public enum LogLevel {
        ERROR, WARNING, INFO, DEBUG
    }

    public enum MockStrategy {
        DEFAULT, RANDOM, RESOURCE
    }
}
