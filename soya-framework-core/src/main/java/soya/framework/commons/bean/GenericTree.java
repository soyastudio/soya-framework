package soya.framework.commons.bean;

public class GenericTree<T> extends TreeBase<T> {

    public GenericTree() {
    }

    public GenericTree(String name, T data) {
        super(name, data);
    }

    public void setRoot(String name, T data) {
        this.setRoot(new DefaultTreeNode<>(name, data));
    }
}
