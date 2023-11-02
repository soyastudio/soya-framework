package soya.framework.restruts;

import javax.servlet.http.HttpServletRequest;

public interface RestActionContext {

    DependencyInjector getDependencyInjector();

    RestActionFactory getActionFactory();

    Serializer getSerializer(String mediaType);

    ActionMapping[] getActionMappings();
    ActionMapping getActionMapping(HttpServletRequest request);

    String getApiPath();
    String getApi();

}
