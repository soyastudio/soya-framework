package soya.framework.action;

import java.io.Serializable;
import java.util.Objects;

public final class ActionDomain implements Comparable<ActionDomain>, Serializable {
    private final String name;
    private final String path;
    private final String title;
    private final String description;

    private ActionDomain(String name, String path, String title, String description) {
        this.name = name;
        this.path = path;
        this.title = title;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionDomain)) return false;
        ActionDomain that = (ActionDomain) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(ActionDomain o) {
        return this.path.compareTo(o.path);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String path;
        private String title;
        private String description;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder fromAnnotation(Domain domain) {
            return this
                    .name(domain.name())
                    .path(domain.path())
                    .title(domain.title())
                    .description(domain.description());
        }

        public ActionDomain create() {
            Objects.requireNonNull(name, "Domain name is required.");
            if (path == null) {
                path = "/" + name;
            }

            if (title == null) {
                title = name;
            }

            if (description == null) {
                description = "";
            }

            ActionDomain domain = new ActionDomain(name, path, title, description);

            return domain;
        }
    }
}
