package soya.framework.restruts;

public enum ParamType {
    PATH_PARAM,
    QUERY_PARAM,
    HEADER_PARAM,
    COOKIE_PARAM,
    FORM_PARAM,
    MATRIX_PARAM,
    BEAN_PARAM,
    PAYLOAD,
    AUTO_WIRED,
    WIRED_SERVICE,
    WIRED_PROPERTY,
    WIRED_RESOURCE;
}
