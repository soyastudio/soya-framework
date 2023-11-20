package soya.framework.action.rest;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionRegistration;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.restruts.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class ActionRestAdapter implements RestActionLoader {

    private ActionRegistration actionRegistration;
    private Set<String> excludes = new HashSet<>();

    public ActionRestAdapter(ActionRegistration actionRegistration) {
        this.actionRegistration = actionRegistration;
    }

    public ActionRestAdapter exclude(String... domains) {
        excludes.addAll(Arrays.asList(domains));
        return this;
    }

    @Override
    public Set<ActionMapping> load() {
        Set<ActionMapping> set = new HashSet<>();
        Arrays.stream(actionRegistration.actions(null)).forEach(e -> {
            if (!excludes.contains(e.getDomain())) {
                set.add(map(actionRegistration.actionType(e)));
            }
        });

        return set;
    }

    private ActionMapping map(Class<? extends Callable> cls) {
        ActionDefinition annotation = cls.getAnnotation(ActionDefinition.class);

        String id = "soya-" + annotation.domain().replace(".", "_").replace("-", "_") + "-" + annotation.name();

        ActionMapping.Builder builder = ActionMapping.builder()
                .id(id)
                .action(annotation.domain() + "://" + annotation.name())
                .actionClass(ActionDispatchAction.class)
                .path("/soya/" + annotation.domain() + "/" + annotation.name())
                .method(HttpMethod.POST)
                .consumes(new String[]{MediaType.TEXT_PLAIN})
                .produces(new String[]{MediaType.TEXT_PLAIN})
                .tags(new String[]{tag(annotation.domain())});

        Arrays.stream(ReflectUtils.getFields(cls)).forEach(field -> {
            if (!Modifier.isStatic(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())
                    && field.getAnnotation(ActionParameter.class) != null) {
                ActionParameter parameter = field.getAnnotation(ActionParameter.class);
                if(!parameter.type().isWired()) {
                    builder.addParameter(field.getName(),
                            getParamType(parameter.type()),
                            parameter.referredTo(),
                            parameter.required(),
                            parameter.description());

                }
            }
        });

        Arrays.stream(annotation.parameters()).forEach(param -> {
            if(!param.type().isWired()) {
                builder.addParameter(param.name(),
                        getParamType(param.type()),
                        param.referredTo(),
                        param.required(),
                        param.description());

            }
        });

        return builder.create();
    }

    private ParamType getParamType(ActionParameterType apt) {
        switch (apt) {
            case INPUT:
                return ParamType.PAYLOAD;

            default:
                return ParamType.HEADER_PARAM;
        }
    }

    private String tag(String domain) {
        String st = domain.replace(".", "_");
        StringTokenizer tokenizer = new StringTokenizer(st, "_");
        StringBuilder builder = new StringBuilder("Soya");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            builder.append(" ")
                    .append(Character.toUpperCase(token.charAt(0)))
                    .append(token.substring(1));

        }

        return builder.toString();
    }
}
