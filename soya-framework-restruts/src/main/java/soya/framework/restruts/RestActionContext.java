package soya.framework.restruts;

import javax.servlet.http.HttpServletRequest;

public interface RestActionContext {

    String getWiredProperty(String propName);

    Object getWiredService(String name);

    <T> T getWiredService(String name, Class<T> type);

    <T> T getResource(String url, Class<T> type);

    ActionMapping[] getActionMappings();

    ActionMapping getActionMapping(HttpServletRequest request);

    Serializer getSerializer(String mediaType);

    String getApiPath();

    String getApi();

}
