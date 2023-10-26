package soya.framework.restruts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RestActionRegistration {
    private Map<RestActionRegistry, Class<? extends Callable>> registrations = new HashMap();

    public Class<? extends Callable> getActionClass(String path, HttpMethod method) {
        return null;
    }

}
