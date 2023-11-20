package soya.framework.commons.bean.expression;

import java.io.Serializable;

public class FunctionalExpression implements Serializable {

    private String name;
    private String[] parameters;

    public FunctionalExpression(String name, String[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name).append("(");
        for (int i = 0; i < getParameters().length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(parameters[i]);
        }
        builder.append(")");
        return builder.toString();
    }

    static FunctionalExpression parse(String exp) {
        String token = exp.trim();
        String name = token.substring(0, token.indexOf("("));
        String[] params = token.substring(token.indexOf("(") + 1, token.lastIndexOf(")")).split(",");
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].trim();
        }
        return new FunctionalExpression(name, params);
    }

}
