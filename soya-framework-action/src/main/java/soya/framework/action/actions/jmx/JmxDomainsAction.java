package soya.framework.action.actions.jmx;

import soya.framework.action.ActionDefinition;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@ActionDefinition(
        domain = "jmx",
        name = "domains"
)
public class JmxDomainsAction extends JmxAction<String[]> {

    @Override
    public String[] execute() throws Exception {
        MBeanServer server;
        return ManagementFactory.getPlatformMBeanServer().getDomains();
    }
}
