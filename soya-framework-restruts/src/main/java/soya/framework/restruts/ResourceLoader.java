package soya.framework.restruts;

public interface ResourceLoader {
    Resource load(String location) throws ResourceException;
}
