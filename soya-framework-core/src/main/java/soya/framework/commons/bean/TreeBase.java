package soya.framework.commons.bean;

import java.util.*;
import java.util.stream.Stream;

public abstract class TreeBase<T> extends AnnotatableFeature implements Tree<T> {
    public static final String PATH_SEPARATOR = "/";

    private TreeNode<T> root;
    private Map<String, TreeNode<T>> nodeMap = new LinkedHashMap<>();

    protected TreeBase() {
    }

    protected TreeBase(TreeNode<T> root) {
        this.root = root;
        nodeMap.put(root.getPath(), root);
    }

    public TreeBase(String name, T data) {
        this.root = new DefaultTreeNode<T>(name, data);
        nodeMap.put(root.getPath(), root);
    }

    protected void setRoot(TreeNode<T> root) {
        if(this.root != null) {
            throw new IllegalStateException("Root node is already set.");

        } else if(root.getParent() != null) {
            throw new IllegalArgumentException("Root node cannot have parent.");

        } else {
            this.root = root;
            nodeMap.put(root.getPath(), root);

        }
    }

    @Override
    public TreeNode<T> root() {
        return root;
    }

    @Override
    public TreeNode<T> add(String path, T data) {
        if(root.getPath().equals(path)) {
            return null;
        }

        String parentPath = path.substring(0, path.lastIndexOf("/"));
        if (nodeMap.containsKey(parentPath)) {
            String name = path.substring(path.lastIndexOf("/") + 1);
            return add(nodeMap.get(parentPath), name, data);

        }

        return null;
    }

    @Override
    public TreeNode<T> add(TreeNode<T> parent, String name, T data) {
        TreeNode<T> node = create(parent, name, data);
        parent.getChildren().add(node);

        nodeMap.put(node.getPath(), node);
        return node;
    }

    @Override
    public boolean contains(String path) {
        return nodeMap.containsKey(path);
    }

    @Override
    public TreeNode get(String path) {
        return nodeMap.get(path);
    }

    @Override
    public Iterator<String> paths() {
        return nodeMap.keySet().iterator();
    }

    @Override
    public Iterator<TreeNode<T>> nodes() {
        return nodeMap.values().iterator();
    }

    @Override
    public Stream<TreeNode<T>> stream() {
        return nodeMap.values().stream();
    }

    protected TreeNode<T> create(TreeNode<T> parent, String name, T data) {
        return new DefaultTreeNode<>(parent, name, data);
    }

    public static class DefaultTreeNode<T> extends AnnotatableFeature implements TreeNode<T> {

        private transient TreeNode<T> parent;
        private String name;
        private T data;
        private List<TreeNode<T>> children = new ArrayList<>();

        private String path;

        public DefaultTreeNode(String name, T data) {
            this(null, name, data);
        }

        public DefaultTreeNode(TreeNode parent, String name, T data) {
            this.parent = parent;
            this.name = name;
            this.data = data;
            this.path = parent == null ? name : parent.getPath() + "/" + name;

        }

        @Override
        public TreeNode<T> getParent() {
            return parent;
        }

        @Override
        public List<TreeNode<T>> getChildren() {
            return children;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public T getData() {
            return data;
        }
    }
}
