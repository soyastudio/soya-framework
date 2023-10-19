package soya.framework.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class ActionResults {

    private static Map<ActionName, AtomicLong> COUNTS = new ConcurrentHashMap<>();
    private static final AtomicLong TOTAL_ACTION_COUNT = new AtomicLong();
    // private static final AtomicLong TOTAL_PRIMITIVE_ACTION_COUNT = new AtomicLong();

    private ActionResults() {
    }

    public static ActionResult create(ActionCallable action, Object result) {
        return new DefaultActionResult(action.actionName(), result);
    }

    private static final class DefaultActionResult implements ActionResult {

        private final long timestamp;
        private final ActionName actionName;
        private final Object result;

        private DefaultActionResult(ActionName actionName, Object result) {
            this.timestamp = System.currentTimeMillis();
            this.actionName = actionName;
            this.result = result;
        }

        @Override
        public ActionName actionName() {
            return actionName;
        }

        @Override
        public Object get() {
            return result;
        }

        @Override
        public boolean success() {
            return result == null || !(result instanceof Throwable);
        }
    }
}
