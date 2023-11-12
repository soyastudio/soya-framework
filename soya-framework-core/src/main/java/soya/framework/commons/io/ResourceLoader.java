package soya.framework.commons.io;

public interface ResourceLoader {
    Resource load(String location) throws ResourceException;
}
