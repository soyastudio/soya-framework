package soya.framework.action.orchestration;

public class Task {
    private final String name;
    private final String uri;



    public Task(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public boolean process(Session session) {
        System.out.println("==================== " + name);
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String uri;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Task create() {
            return new Task(name, uri);
        }
    }
}
