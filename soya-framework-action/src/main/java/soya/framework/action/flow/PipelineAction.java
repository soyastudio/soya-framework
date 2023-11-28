package soya.framework.action.flow;

import soya.framework.action.ActionName;
import soya.framework.action.ActionParameterType;
import soya.framework.action.DynaAction;
import soya.framework.action.util.ConvertUtils;

import java.util.*;
import java.util.concurrent.Callable;

public abstract class PipelineAction<T> implements DynaAction, Callable<T> {

    private final Pipeline pipeline;
    private final Map<String, Wrapper> params;

    public PipelineAction(Pipeline pipeline) {
        this.pipeline = pipeline;
        Map<String, Wrapper> map = new HashMap<>();
        Arrays.stream(pipeline.parameterNames()).forEach(e -> {
            if (!pipeline.parameterType(e).isWired()) {
                map.put(e, new Wrapper());
            }
        });

        params = Collections.unmodifiableMap(map);
    }

    @Override
    public String[] parameterNames() {
        return params.keySet().toArray(new String[params.size()]);
    }

    @Override
    public ActionParameterType parameterType(String paramName) {
        return pipeline.parameterType(paramName);
    }

    @Override
    public boolean required(String paramName) {
        return pipeline.required(paramName);
    }

    public void setParameter(String name, Object value) {
        if (!params.containsKey(name)) {
            params.get(name).set(value);
        }
    }

    @Override
    public T call() throws Exception {
        Session session = new DefaultSession(pipeline, params);

        return null;
    }

    private static class Wrapper {
        private Object value;

        private Wrapper() {
        }

        private Wrapper(Object value) {
            this.value = value;
        }

        public void set(Object value) {
            this.value = value;
        }

        public Object get() {
            return value;

        }
    }

    private static class DefaultSession implements Session {
        private final ActionName actionName;
        private final String id;
        private final long startTime;

        private final Map<String, Object> parameters;
        private Map<String, Object> attributes = new HashMap<>();

        private DefaultSession(Pipeline pipeline, Map<String, Wrapper> parameters) {
            this.actionName = pipeline.getActionName();
            this.id = UUID.randomUUID().toString();
            this.startTime = System.currentTimeMillis();

            Map<String, Object> paramMap = new LinkedHashMap<>();
            if (parameters != null) {
                parameters.entrySet().forEach(e -> {
                    if (!pipeline.parameterType(e.getKey()).isWired()) {
                        Class<?> type = pipeline.type(e.getKey());
                        paramMap.put(e.getKey(),
                                ConvertUtils.convert(e.getValue().get(), type));
                    }

                });
            }
            this.parameters = Collections.unmodifiableMap(paramMap);

        }

        @Override
        public ActionName getActionName() {
            return actionName;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public long startTime() {
            return startTime;
        }

        @Override
        public Object getParameter(String name) {
            return parameters.get(name);
        }

        @Override
        public Object get(String attrName) {
            return attributes.get(attrName);
        }

        @Override
        public void set(String attrName, Object attrValue) {
            if (attrValue == null) {
                attributes.remove(attrName);
            } else {
                attributes.put(attrName, attrValue);
            }
        }

    }


}
