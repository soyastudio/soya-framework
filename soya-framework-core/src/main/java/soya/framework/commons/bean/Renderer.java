package soya.framework.commons.bean;

import java.io.OutputStream;

public interface Renderer<T extends Annotatable> {
    void render(T object, OutputStream out);
}
