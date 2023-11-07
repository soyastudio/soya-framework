package soya.framework.action.actions.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class JmxMBeansAction extends JmxAction<String[]> {

    @Override
    public String[] execute() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("java.lang:*");
        Set<ObjectInstance> instances = server.queryMBeans(null, null);

        List<String> list = new ArrayList<>();

        instances.forEach(e -> {
            list.add(e.getObjectName().toString());
        });
        Collections.sort(list);

        return list.toArray(new String[list.size()]);
    }
}
