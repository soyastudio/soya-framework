package soya.framework.action;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class ActionBean {
    private final ActionCallable action;
    private final ActionDescription actionDescription;

    ActionBean(ActionCallable action, ActionDescription desc) {
        this.action = action;
        this.actionDescription = desc;
    }

    public ActionName getActionName() {
        return actionDescription.getActionName();
    }

    public ActionDescription getActionDescription() {
        return actionDescription;
    }

    public ActionCallable getAction() {
        return action;
    }

    public String[] getPropertyNames() {
        return actionDescription.getActionPropertyNames();
    }

    public Class<?> getPropertyType(String propertyName) {
        return actionDescription.getActionPropertyDescription(propertyName).getPropertyType();
    }

    public void set(String propertyName, Object value) {
        if (ActionClass.get(actionDescription.getActionName()) != null) {
            Field field = ActionClass.get(actionDescription.getActionName()).getActionField(propertyName);
            field.setAccessible(true);
            try {
                field.set(action, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                PropertyUtils.setProperty(action, propertyName, value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
