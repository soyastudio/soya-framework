package soya.framework.action.orchestration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AnnotatedPipelineAction <T> extends AnnotatedDynaAction<T> {

    protected Map<String, Task> tasks = new LinkedHashMap<>();

    public AnnotatedPipelineAction() {
        super();

        PipelineDefinition annotation = getClass().getAnnotation(PipelineDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Pipeline annotation is required.");
        }

        Arrays.stream(annotation.tasks()).forEach(e -> {

        });

    }

    @Override
    public T call() throws Exception {
        Session session = new DefaultSession(actionName, parameters);

        return null;
    }
}
