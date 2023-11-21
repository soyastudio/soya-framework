package soya.framework.gherkin;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ScenarioTestRunner implements Callable<ScenarioTestResult> {
    private Scenario scenario;

    private Method method;
    private Object instance;

    public ScenarioTestRunner(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public ScenarioTestResult call() {
        return null;
    }
}
