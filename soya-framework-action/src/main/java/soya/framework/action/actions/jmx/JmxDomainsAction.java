package soya.framework.action.actions.jmx;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

public class JmxDomainsAction extends JmxAction<String[]> {

    @Override
    public String[] execute() throws Exception {
        MBeanServer server;
        return ManagementFactory.getPlatformMBeanServer().getDomains();
    }
}
