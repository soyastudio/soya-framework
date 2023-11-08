package soya.framework.restruts;

import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {
    void handleException(Exception e, HttpServletResponse response);
}
