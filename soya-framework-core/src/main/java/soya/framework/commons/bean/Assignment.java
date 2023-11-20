package soya.framework.commons.bean;

import java.io.Serializable;
import java.util.Objects;

public final class Assignment implements Serializable {

    private final AssignmentType assignmentType;
    private final String expression;

    public Assignment(String assignment) {
        this.assignmentType = AssignmentType.getAssignmentMethod(assignment);
        this.expression = assignment.substring(assignment.indexOf('(') + 1, assignment.lastIndexOf(')')).trim();
    }

    public Assignment(AssignmentType assignmentType, String expression) {
        this.assignmentType = assignmentType;
        this.expression = expression;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return assignmentType.toString(expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return assignmentType == that.assignmentType && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentType, expression);
    }

    public enum AssignmentType {
        VALUE("val"),
        PARAMETER("param"),
        RESOURCE("res"),
        REFERENCE("ref");

        private final String function;

        AssignmentType(String function) {
            this.function = function;
        }

        public String toString(String expression) {
            return function + "(" + expression + ")";
        }

        public static AssignmentType getAssignmentMethod(String expression) {
            String token = expression.trim();
            if (token.indexOf('(') > 0 && token.endsWith(")")) {
                token = token.substring(0, token.indexOf('('));
            }

            switch (token) {
                case "val":
                    return VALUE;
                case "param":
                    return PARAMETER;
                case "res":
                    return RESOURCE;
                case "ref":
                    return REFERENCE;

                default:
                    throw new IllegalArgumentException("Cannot parse expression: " + expression);
            }
        }
    }
}
