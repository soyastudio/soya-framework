package soya.framework.gherkin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GherkinEngine {
    private static GherkinEngine me;
    private final ExecutorService executorService;

    private GherkinEngine(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static GherkinEngine getInstance() {
        if(me == null) {
            throw new IllegalStateException("GherkinEngine is not initialized!");
        }
        return me;
    }

    public ScenarioTestResult execute(Scenario scenario, long timeout) throws ExecutionException, InterruptedException {
        long timestamp = System.currentTimeMillis();
        Future<ScenarioTestResult> resultFuture = executorService.submit(new ScenarioTestRunner(scenario));
        while (!resultFuture.isDone() && System.currentTimeMillis() - timestamp < timeout) {
            try {
                Thread.sleep(300l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return resultFuture.get();

    }

    public static Initializer initializer() {
        if (me != null) {
            throw new IllegalStateException("GherkinEngine already exist.");
        }

        return new Initializer();
    }

    public static class Initializer {
        private ExecutorService executorService;

        public Initializer executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public GherkinEngine create() {
            if(executorService == null) {
                this.executorService = Executors.newSingleThreadExecutor();
            }

            me = new GherkinEngine(executorService);
            return me;
        }

    }


}
