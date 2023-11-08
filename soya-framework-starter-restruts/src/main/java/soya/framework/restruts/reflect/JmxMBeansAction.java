package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestAction(
        id = "jmxMBeans",
        path = "/restruts/jmx/mbeans",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct"
)
public class JmxMBeansAction extends ReflectAction {
    @Override
    public Object call() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("java.lang:*");
        Set<ObjectInstance> instances = server.queryMBeans(null, null);

        List<String> list = new ArrayList<>();

        instances.forEach(e -> {
            list.add(e.getObjectName().toString());
        });
        Collections.sort(list);
        return GSON.toJson(list);
    }
}
