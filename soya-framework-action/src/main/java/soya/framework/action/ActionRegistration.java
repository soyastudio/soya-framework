package soya.framework.action;

import java.util.ArrayList;
import java.util.List;

public final class ActionRegistration {

    private ActionRegistration(List<ActionFactory> actionFactories, List<ActionClassScanner> scanners) {
        if(actionFactories != null) {
            actionFactories.forEach(e -> {
                ActionClass.register(e);
            });
        }

        if(scanners != null) {
            scanners.forEach(e -> {
                e.scan();
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ActionFactory> actionFactories = new ArrayList<>();
        private List<ActionClassScanner> scanners = new ArrayList<>();

        private Builder() {
        }

        public Builder register(ActionFactory actionFactory) {
            actionFactories.add(actionFactory);
            return this;
        }

        public Builder register(ActionClassScanner scanner) {
            scanners.add(scanner);
            return this;
        }

        public ActionRegistration create() {
            return new ActionRegistration(actionFactories, scanners);
        }
    }
}
