package soya.framework.commons.bean;

public interface Annotator<T extends Annotatable> {
    void annotate(T annotatable);

}
