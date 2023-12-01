package soya.framework.action.springboot;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.action.*;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.commons.util.ReflectUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class ActionDefinitionAnnotationScanner implements ActionClassScanner {

    private String[] packages;

    public ActionDefinitionAnnotationScanner(String[] packages) {
        this.packages = packages;
    }

    @Override
    public Set<ActionClass> scan() {
        Set<ActionClass> actionClasses = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ActionDefinition.class, true, true));

        Arrays.stream(packages).forEach(pkg -> {
            Set<BeanDefinition> set = scanner.findCandidateComponents(pkg.trim());
            set.forEach(e -> {
                try {
                    Class<? extends Callable> cls = (Class<? extends Callable>) Class.forName(e.getBeanClassName());
                    actionClasses.add(fromActionDefinition(cls));

                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        return actionClasses;
    }

    private ActionClass fromActionDefinition(Class<? extends Callable> actionType) {
        ActionDefinition annotation = actionType.getAnnotation(ActionDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException();
        }

        ActionClass.Builder builder = ActionClass.builder()
                .actionType(actionType)
                .actionName(ActionName.create(annotation.domain(), annotation.name()));
        if (DynaAction.class.isAssignableFrom(actionType)) {
            Arrays.stream(annotation.properties()).forEach(p -> {
                builder.addProperty(ActionProperty.builder()
                        .name(p.name())
                        .type(DefaultUtils.isDefaultType(p.type()) ? Object.class : DefaultUtils.getDefaultType(p.type()))
                        .propertyType(p.propertyType())
                        .referredTo(p.referredTo())
                        .required(p.required())
                        .description(p.description())
                        .create());
            });
        } else {
            // From field annotation:
            Field[] fields = ReflectUtils.getFields(actionType);
            Arrays.stream(fields).forEach(field -> {
                if (!Modifier.isFinal(field.getModifiers())
                        && !Modifier.isStatic(field.getModifiers())
                        && field.getAnnotation(ActionPropertyDefinition.class) != null) {
                    builder.addProperty(field, field.getAnnotation(ActionPropertyDefinition.class));
                }
            });

            // Override From ActionDefinition annotation:
            Arrays.stream(annotation.properties()).forEach(e -> {
                Field field = ReflectUtils.findField(actionType, e.name());
                builder.addProperty(field, e);
            });

        }

        return builder.create();
    }
}
