package soya.framework.action;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public abstract class Action<T> implements ActionCallable {
    public ActionName actionName() {
        return getActionClass().getActionName();
    }

    @Override
    public ActionResult call() {
        logger().fine("start executing...");

        try {
            // Check if all required fields were set.
            checkRequiredProperties();

            // Prepare
            prepare();

            T t = execute();
            ActionResult result = ActionResults.create(this, t);

            logger().fine("executed successfully.");
            return result;

        } catch (Exception e) {
            logger().severe(new StringBuilder()
                    .append(e.getClass().getName())
                    .append("[")
                    .append(e.getMessage())
                    .append("]")
                    .toString());

            return ActionResults.create(this, e);
        }
    }

    public abstract T execute() throws Exception;

    protected ActionClass getActionClass() {
        return ActionClass.get(getClass());
    }

    protected void prepare() throws ActionException {
    }

    protected void checkRequiredProperties() throws ActionException {
        Field[] fields = ActionClass.get(getClass()).getActionFields();
        for (Field field : fields) {
            ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
            if (actionProperty.required()) {
                field.setAccessible(true);
                try {
                    if (field.get(this) == null) {
                        throw new IllegalStateException("Required field '"
                                + field.getName() + "' is not set for action '"
                                + getClass().getName() + "'.");
                    }
                } catch (IllegalAccessException e) {
                    throw new ActionException(e);

                }
            }
        }
    }

    protected ActionContext context() {
        return ActionContext.getInstance();
    }

    protected Logger logger() {
        return Logger.getLogger(getClass().getName());
    }
}
