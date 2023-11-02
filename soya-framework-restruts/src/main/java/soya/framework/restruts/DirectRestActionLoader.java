package soya.framework.restruts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DirectRestActionLoader implements RestActionLoader {
    private Set<Class<?>> classes = new HashSet<>();

    public DirectRestActionLoader add(Class<?> cls) {
        if (cls.getAnnotation(RestAction.class) == null) {
            throw new IllegalArgumentException("Class not annotated as RestActionMapping: " + cls.getName());
        }

        classes.add(cls);
        return this;
    }

    @Override
    public Set<ActionMapping> load() {
        Set<ActionMapping> set = new HashSet<>();
        classes.forEach(e -> {
            RestAction annotation = e.getAnnotation(RestAction.class);
            String id = annotation.id();
            if(id.isEmpty()) {
                id = e.getSimpleName();
            }

            ActionMapping.Builder builder = ActionMapping
                    .builder()
                    .actionClass(e)
                    .action(annotation.action())
                    .method(annotation.method())
                    .path(annotation.path())
                    .id(id)
                    .consumes(annotation.consumes())
                    .produces(annotation.produces())
                    .tags(annotation.tags());

            Arrays.stream(annotation.parameters()).forEach(p -> {
                builder.addParameter(p.name(),
                        p.paramType(),
                        p.referredTo().isEmpty() ? p.name() : p.referredTo(),
                        p.description());
            });

            set.add(builder.create());


        });
        return set;
    }
}
