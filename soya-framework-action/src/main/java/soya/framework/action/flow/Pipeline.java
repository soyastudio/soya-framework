package soya.framework.action.flow;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionName;
import soya.framework.action.ActionParameterType;

import java.util.*;

public final class Pipeline {
    private static Map<ActionName, Pipeline> pipelines = new HashMap<>();
    private ActionName actionName;
    private Map<String, DynamicParameter> parameters = new LinkedHashMap<>();

    private Pipeline(ActionName actionName, Collection<DynamicParameter> parameters) {
        this.actionName = actionName;
        if(parameters != null) {
            parameters.forEach(e -> {
                this.parameters.put(e.name, e);
            });
        }
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

    public PipelineExecutor executor(ActionContext actionContext) {
        return new PipelineExecutor(this, actionContext);
    }

    public static class Builder {
        private ActionName actionName;
        private List<DynamicParameter> parameterList = new ArrayList<>();

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

        public Pipeline create(boolean register) {
            Pipeline pipeline = new Pipeline(actionName, parameterList);
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
}
