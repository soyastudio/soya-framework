package soya.framework.action.rest;

import soya.framework.action.*;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.restruts.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class ActionRestAdapter implements RestActionLoader {

    private ActionRegistration actionRegistration;
    private Set<String> excludes = new HashSet<>();

    private NamingStrategy namingStrategy = new DefaultNamingStrategy();

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
        Arrays.stream(ActionClass.actionNames()).forEach(e -> {
            if (!excludes.contains(e.getDomain())) {
                set.add(map(ActionClass.forName(e)));
            }
        });

        return set;
    }

    private ActionMapping map(ActionClass actionClass) {
        ActionName actionName = actionClass.getActionName();

        String id = "soya-" + actionName.getDomain().replace(".", "_").replace("-", "_") + "-" + actionName.getName();

        ActionMapping.Builder builder = ActionMapping.builder()
                .id(id)
                .action(actionName.getDomain() + "://" + actionName.getName())
                .actionClass(ActionDispatchAction.class)
                .path(namingStrategy.toPath(actionName))
                .method(HttpMethod.POST)
                .consumes(new String[]{MediaType.TEXT_PLAIN})
                .produces(new String[]{MediaType.TEXT_PLAIN})
                .tags(new String[]{namingStrategy.toTag(actionName)});

        Class<?> cls = actionClass.getActionType();
        if(DynaAction.class.isAssignableFrom(cls)) {
            Arrays.stream(ReflectUtils.getFields(cls)).forEach(field -> {
                if (!Modifier.isStatic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers())
                        && field.getAnnotation(ActionParameterDefinition.class) != null) {
                    ActionParameterDefinition parameter = field.getAnnotation(ActionParameterDefinition.class);
                    if(!parameter.parameterType().isWired()) {
                        builder.addParameter(field.getName(),
                                getParamType(parameter.parameterType()),
                                parameter.referredTo(),
                                parameter.required(),
                                parameter.description());

                    }
                }
            });
        }

        Arrays.stream(actionClass.parameterNames()).forEach(param -> {
            if(!actionClass.parameterType(param).isWired()) {

                builder.addParameter(param,
                        getParamType(actionClass.parameterType(param)),
                        actionClass.referredTo(param),
                        actionClass.required(param),
                        actionClass.description(param));

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

    static class DefaultNamingStrategy implements NamingStrategy {
        @Override
        public String toTag(ActionName actionName) {
            String st = actionName.getDomain().replace(".", "_");
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

        @Override
        public String toPath(ActionName actionName) {
            return "/soya/" + actionName.getDomain() + "/" + actionName.getName();
        }

        @Override
        public String toId(ActionName actionName) {
            return "soya-" + actionName.getDomain().replace(".", "_").replace("-", "_") + "-" + actionName.getName();
        }
    }
}
