package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@RestAction(
        id = "service-info",
        path = "/restruts/service-info",
        method = HttpMethod.GET,
        parameters = {
                @RestActionParameter(name = "service", paramType = ParamType.HEADER_PARAM)
        },
        produces = "text/plain",
        tags = "Restruct"
)
public class ServiceInfoAction extends ReflectAction<String> {
    private String service;

    @Override
    public String call() throws Exception {
        Object o = getRestActionContext().getDependencyInjector().getWiredResource(service);

        StringBuilder sb = new StringBuilder();
        Method[] methods = o.getClass().getDeclaredMethods();
        Arrays.stream(methods).forEach(m -> {

            printMethod(m, sb);
            sb.append("\n");
        });

        return sb.toString();
    }

    private void printMethod(Method method, StringBuilder sb) {
        sb.append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName())
                .append("(");

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i ++) {
            if(i > 0) {
                sb.append(",");
            }
            sb.append(parameters[i].getType().getName());
        }

        sb.append(")");
    }
}
