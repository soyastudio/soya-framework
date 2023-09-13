package soya.framework.action;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

public final class ActionName implements Comparable<ActionName>, Serializable {

    private final String domain;
    private final String name;

    private ActionName(String domain, String name) {

        this.domain = domain;
        this.name = name;

        if (domain == null || domain.trim().length() == 0) {
            throw new IllegalArgumentException("ActionName Syntax Error: domain cannot be null or empty");
        }

        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("ActionName Syntax Error: name cannot be null or empty");
        }
    }

    public static ActionName fromURI(URI uri) {
        return new ActionName(uri.getScheme(), uri.getHost());
    }

    public static ActionName create(String domain, String name) {
        return new ActionName(domain, name);
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return domain + "://" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionName)) return false;
        ActionName that = (ActionName) o;
        return Objects.equals(domain, that.domain) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, name);
    }

    @Override
    public int compareTo(ActionName o) {
        int result = domain.compareTo(o.domain);
        if(result == 0) {
            result = name.compareTo(o.name);
        }
        return result;
    }
}
