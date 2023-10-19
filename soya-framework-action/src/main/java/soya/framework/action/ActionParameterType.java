package soya.framework.action;

public enum ActionParameterType {
    PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD;

    private static final ActionParameterType[] SEQUENCE
            = {PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD};

    public static final int index(ActionParameterType type) {
        int i = 0;
        for (ActionParameterType p : SEQUENCE) {
            if (p.equals(type)) {
                return i;
            } else {
                i++;
            }
        }

        return -1;
    }
}
