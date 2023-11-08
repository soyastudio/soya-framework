package soya.framework.restruts.springboot;

import soya.framework.restruts.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public void handleException(Exception e, HttpServletResponse response) {
        System.out.println("caught exception: " + e.getMessage());
    }
}
