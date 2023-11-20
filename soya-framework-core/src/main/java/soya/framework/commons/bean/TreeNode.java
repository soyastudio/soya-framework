package soya.framework.commons.bean;

import java.util.List;

public interface TreeNode<T> extends Annotatable {

    TreeNode<T> getParent();

    List<TreeNode<T>> getChildren();

    String getName();

    String getPath();

    T getData();

}
