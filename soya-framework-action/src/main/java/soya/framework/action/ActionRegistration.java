package soya.framework.action;

public class ActionRegistration {

    public ActionRegistration register(ActionFactory factory) {
        ActionClass.register(factory);
        return this;
    }

    public ActionRegistration load(ActionClassScanner scanner) {
        scanner.scan().forEach(e -> {
            ActionClass.register(e);
        });
        return this;
    }
}
