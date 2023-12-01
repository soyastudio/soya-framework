package soya.framework.action;

public final class ActionRegistration {

    public ActionRegistration register(ActionFactory factory) {
        ActionClass.register(factory);
        return this;
    }

    public ActionRegistration load(ActionClassScanner scanner) {
        scanner.scan();
        return this;
    }
}
