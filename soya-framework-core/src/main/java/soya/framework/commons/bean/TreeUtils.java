package soya.framework.commons.bean;

public class TreeUtils {
    private TreeUtils() {
    }

    public static String print(Tree tree) {
        StringBuilder builder = new StringBuilder();
        print(tree.root(), builder, 0);
        return builder.toString();
    }

    private static void print(TreeNode<?> node, StringBuilder builder, int indent) {
        printIndents(builder, indent);
        builder.append(node.getName()).append("\n");
        node.getChildren().forEach(n -> {
            print(n, builder, indent + 1);
        });
    }

    private static void printIndents(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append("\t");
        }
    }

}
