package soya.framework.restruts;

import javax.servlet.http.HttpServletRequest;

public interface RestActionContext {

    String getProperty(String propName);

    Object getService(String name);

    <T> T getService(String name, Class<T> type);

    <T> T getResource(String url, Class<T> type);

    ActionMapping[] getActionMappings();

    ActionMapping getActionMapping(HttpServletRequest request);

    Serializer getSerializer(String mediaType);

    ExceptionHandler getExceptionHandler();

    String getApiPath();

    String getApi();

}
