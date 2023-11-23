package soya.framework.action.flow;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionName;
import soya.framework.action.ActionParameterType;

import java.net.URI;
import java.util.*;

public final class Pipeline {

    private static Map<ActionName, Pipeline> pipelines = new HashMap<>();

    private ActionName actionName;
    private Map<String, DynamicParameter> parameters = new LinkedHashMap<>();
    private List<Task> tasks = new ArrayList<>();

    private Pipeline(ActionName actionName, Collection<DynamicParameter> parameters, List<Task> tasks) {
        this.actionName = actionName;
        if (parameters != null) {
            parameters.forEach(e -> {
                this.parameters.put(e.name, e);
            });
        }

        this.tasks.addAll(tasks);
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String[] parameterNames() {
        return parameters.keySet().toArray(new String[parameters.size()]);
    }

    public Class<?> type(String name) {
        return parameters.get(name).type;
    }

    public ActionParameterType parameterType(String name) {
        return parameters.get(name).parameterType;
    }

    public static PipelineExecutor executor(Object input, ActionName actionName, ActionContext actionContext) {
        if(!pipelines.containsKey(actionName)) {
            throw new IllegalArgumentException("Pipeline is not defined: " + actionName);
        }

        Pipeline pipeline = pipelines.get(actionName);
        return new PipelineExecutor(startSession(input, pipelines.get(actionName), actionContext), pipeline.tasks, actionContext);
    }

    private static Session startSession(Object input, Pipeline pipeline, ActionContext actionContext) {
        return new DefaultSession(input, pipeline, actionContext);
    }

    private static class DefaultSession implements Session {

        private final ActionName actionName;
        private final String id;
        private final long startTime;

        private Map<String, Object> parameters = new LinkedHashMap<>();
        private Map<String, Object> attributes = new HashMap<>();

        private DefaultSession(Object input, Pipeline pipeline, ActionContext actionContext) {
            this.actionName = pipeline.getActionName();
            this.id = UUID.randomUUID().toString();
            this.startTime = System.currentTimeMillis();

            parameters.putAll(actionContext.getService(null, Consumer.class).consume(input, pipeline));

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

    public static class Builder {
        private ActionName actionName;
        private List<DynamicParameter> parameterList = new ArrayList<>();
        private List<Task> tasks = new ArrayList<>();

        private Builder() {
        }

        public Builder name(String domain, String name) {
            this.actionName = ActionName.create(domain, name);
            return this;
        }

        public Builder addParameter(ParameterBuilder parameterBuilder) {
            parameterList.add(parameterBuilder.create());
            return this;
        }

        public Builder addTask(TaskBuilder builder) {
            return this;
        }

        public Builder addTask(URI uri) {
            return this;
        }

        public Pipeline create(boolean register) {
            Pipeline pipeline = new Pipeline(actionName, parameterList, tasks);
            if (register) {
                pipelines.put(pipeline.actionName, pipeline);
            }
            return pipeline;
        }
    }

    public static class ParameterBuilder {
        private String name;

        private Class<?> type;

        private ActionParameterType parameterType;

        private String referredTo;
        private boolean required;
        private String description;

        private ParameterBuilder() {
        }

        public ParameterBuilder name() {
            this.name = name;
            return this;
        }

        Pipeline.DynamicParameter create() {
            return new Pipeline.DynamicParameter(name, type, parameterType, referredTo, required, description);
        }
    }

    static class DynamicParameter {
        private final String name;

        private final Class<?> type;

        private final ActionParameterType parameterType;

        private final String referredTo;
        private final boolean required;
        private final String description;

        DynamicParameter(String name,
                         Class<?> type,
                         ActionParameterType parameterType,
                         String referredTo,
                         boolean required,
                         String description) {
            this.name = name;
            this.type = type;
            this.parameterType = parameterType;
            this.referredTo = referredTo;
            this.required = required;
            this.description = description;
        }
    }

    static class TaskBuilder {
        private String name;
        private ActionName actionName;

        private TaskBuilder() {
        }

        public TaskBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Task create() {
            return new Task();
        }

    }
}
