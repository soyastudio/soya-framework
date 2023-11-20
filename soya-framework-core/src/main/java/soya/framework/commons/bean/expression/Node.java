package soya.framework.commons.bean.expression;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Node {
    private Map<String, FunctionalExpression> functions = new LinkedHashMap<>();

    public boolean contains(String func) {
        return functions.containsKey(func);
    }

    public String[] getFunctionParameters(String func) {
        if (contains(func)) {
            return functions.get(func).getParameters();
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        functions.values().forEach(e -> {
            builder.append(e).append("::");
        });

        String st = builder.toString();
        if(st.endsWith("::")) {
            st = st.substring(0, st.length() - 2);
        }

        return st;
    }

    public static Node fromString(String expression) {
        Node node = new Node();
        String[] array = expression.split("::");
        Arrays.stream(array).forEach(e -> {
            FunctionalExpression fe = FunctionalExpression.parse(e);
            node.functions.put(fe.getName(), fe);
        });

        return node;
    }
}
