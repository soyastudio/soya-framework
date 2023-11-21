package soya.framework.action.flow;

import soya.framework.action.ActionName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class PipelineAction<T> implements Callable<T> {

    protected PipelineAction() {
    }

    @Override
    public T call() throws Exception {
        return process(new PipelineSession());
    }

    protected abstract T process(Session session);

    protected static class PipelineSession implements Session {
        private final String id;

        private Map<String, Object> parameters = new LinkedHashMap<>();

        private Map<String, Object> attributes = new HashMap<>();

        protected PipelineSession() {
            this.id = UUID.randomUUID().toString();
        }

        @Override
        public ActionName getActionName() {
            return null;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
