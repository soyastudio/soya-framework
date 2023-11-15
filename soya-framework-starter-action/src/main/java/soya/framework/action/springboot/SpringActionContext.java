package soya.framework.action.springboot;

import org.springframework.context.ApplicationContext;
import soya.framework.action.ActionContext;
import soya.framework.action.ServiceNotFoundException;

public class SpringActionContext implements ActionContext {

    private ApplicationContext applicationContext;

    public SpringActionContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getProperty(String propName) {
        return applicationContext.getEnvironment().getProperty(propName);
    }

    @Override
    public Object getService(String name) throws ServiceNotFoundException {
        try {
            return applicationContext.getBean(name);

        } catch (Exception e) {
            throw new ServiceNotFoundException(e);
        }
    }

    @Override
    public <T> T getService(String name, Class<T> type) throws ServiceNotFoundException {
        try {
            if (name == null) {
                return applicationContext.getBean(type);
            } else {
                return applicationContext.getBean(name, type);
            }
        } catch (Exception e) {
            throw new ServiceNotFoundException(e);
        }
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        return null;
    }
}
