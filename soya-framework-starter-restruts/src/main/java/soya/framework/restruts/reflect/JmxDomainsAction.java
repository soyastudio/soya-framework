package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@RestAction(
        id = "jmxDomains",
        path = "/restruts/jmx/domains",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct"
)
public class JmxDomainsAction extends ReflectAction {
    @Override
    public String call() throws Exception {
        MBeanServer server;
        String[] domains = ManagementFactory.getPlatformMBeanServer().getDomains();
        return GSON.toJson(domains);
    }
}
