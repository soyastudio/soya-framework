package soya.framework.commons.bean;

import java.util.Iterator;
import java.util.stream.Stream;

public interface Tree<T> {

    TreeNode<T> root();

    TreeNode<T> add(String path, T data);

    TreeNode<T> add(TreeNode<T> parent, String name, T data);

    boolean contains(String path);

    TreeNode<T> get(String path);

    Iterator<String> paths();

    Iterator<TreeNode<T>> nodes();

    Stream<TreeNode<T>> stream();
}
