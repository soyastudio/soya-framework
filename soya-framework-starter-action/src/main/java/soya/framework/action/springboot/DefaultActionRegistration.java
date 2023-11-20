package soya.framework.action.springboot;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.action.*;
import soya.framework.commons.util.ReflectUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Callable;

public class DefaultActionRegistration implements ActionRegistration {
    private Set<String> domains = new LinkedHashSet<>();
    private Map<ActionName, Class<?>> registration = new TreeMap<>();
    private ClassPathScanningCandidateComponentProvider scanner;

    DefaultActionRegistration() {
        scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ActionDefinition.class, true, true));
    }

    @Override
    public String[] domains() {
        List<String> list = new ArrayList<>(domains);
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public ActionName[] actions(String domain) {
        List<ActionName> list = new ArrayList<>();
        if (domain == null) {
            list.addAll(registration.keySet());
        } else {
            registration.keySet().forEach(e -> {
                if (e.getDomain().equals(domain)) {
                    list.add(e);
                }
            });

        }
        Collections.sort(list);
        return list.toArray(new ActionName[list.size()]);
    }

    @Override
    public Class<? extends Callable> actionType(ActionName actionName) {
        return (Class<? extends Callable>) registration.get(actionName);
    }

    public ActionBean actionDetails(String uri) {
        try {
            ActionName actionName = ActionName.fromURI(new URI(uri));

            return new ActionBean(registration.get(actionName));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Action is not defined for: " + uri);
        }
    }

    void register(String... packages) {
        Arrays.stream(packages).forEach(pkg -> {
            Set<BeanDefinition> set = scanner.findCandidateComponents(pkg.trim());
            set.forEach(e -> {
                try {
                    Class<? extends Callable> cls = (Class<? extends Callable>) Class.forName(e.getBeanClassName());
                    ActionName actionName = ActionClass.register(cls);
                    domains.add(actionName.getDomain());
                    registration.put(actionName, cls);

                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
    }

    public static class ActionBean {
        private String domain;
        private String name;
        private String description;
        private String actionClass;

        private List<ActionProperty> properties = new ArrayList<>();

        ActionBean(Class<?> cls) {
            ActionDefinition definition = cls.getAnnotation(ActionDefinition.class);
            this.domain = definition.domain();
            this.name = definition.name();
            this.description = definition.description();
            this.actionClass = cls.getName();

            Map<String, ActionParameter> overrides = new LinkedHashMap<>();
            Arrays.stream(definition.parameters()).forEach(p -> {
                overrides.put(p.name(), p);
            });

            Arrays.stream(ReflectUtils.getFields(cls)).forEach(f -> {
                if (overrides.containsKey(f.getName())) {
                    ActionParameter parameter = overrides.get(f.getName());
                    properties.add(new ActionProperty(f.getName(),
                            f.getType().getName(),
                            parameter.required(),
                            parameter.type(),
                            parameter.referredTo(),
                            parameter.description()));

                } else if (f.getAnnotation(ActionParameter.class) != null) {
                    ActionParameter parameter = f.getAnnotation(ActionParameter.class);
                    properties.add(new ActionProperty(f.getName(),
                            f.getType().getName(),
                            parameter.required(),
                            parameter.type(),
                            parameter.referredTo(),
                            parameter.description()));
                }
            });
        }

    }

    public static class ActionProperty {
        private final String name;

        private final String paramClass;

        private final boolean required;
        private final ActionParameterType paramType;

        private final String referredTo;

        private final String description;

        ActionProperty(String name, String paramClass, boolean required, ActionParameterType paramType, String referredTo, String description) {
            this.name = name;
            this.paramClass = paramClass;
            this.required = required;
            this.paramType = paramType;
            this.referredTo = referredTo;
            this.description = description;
        }
    }
}
