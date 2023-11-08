package soya.framework.action;

public interface ResourceLoader {
    Resource load(String location) throws ResourceException;
}
