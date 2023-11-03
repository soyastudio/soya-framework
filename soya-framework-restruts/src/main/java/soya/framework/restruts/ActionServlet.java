package soya.framework.restruts;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.util.ConvertUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ActionServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ActionServlet.class.getName());

    private RestActionContext context;

    public ActionServlet(RestActionContext context) {
        super();
        this.context = context;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = actionPath(req);
        if (path.equals(context.getApiPath())) {
            resp.getOutputStream().write(context.getApi().getBytes());

        } else if (path != null && (path.endsWith(".png")
                || path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js"))) {
            forward(path, resp);

        } else {
            dispatch(req, resp);
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    protected void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ActionMapping actionMapping = context.getActionMapping(req);

        if (actionMapping == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

        } else {

            Callable<?> callable = create(actionMapping, req);
            AsyncContext asyncContext = req.startAsync();

            String contentType = req.getHeader("Content-Type");
            if (contentType == null) {
                if (actionMapping.getProduces().length > 0) {
                    contentType = actionMapping.getProduces()[0];
                } else {
                    contentType = "text/plain";
                }
            }
            resp.setHeader("Content-Type", contentType);
            Serializer serializer = context.getSerializer(contentType);

            asyncContext.start(() -> {
                try {
                    Object result = callable.call();
                    if (result != null) {
                        OutputStream out = resp.getOutputStream();
                        out.write(serializer.serialize(result));
                        out.flush();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);

                } finally {
                    asyncContext.complete();
                }
            });
        }
    }

    private Callable<?> create(ActionMapping mapping, HttpServletRequest request) {
        try {
            Class<?> cls = mapping.getActionClass();
            Callable<?> callable = (Callable<?>) cls.newInstance();

            String accept = request.getHeader("accept");
            if (accept == null) {
                if (mapping.getConsumes().length > 0) {
                    accept = mapping.getConsumes()[0];
                } else {
                    accept = "text/plain";
                }
            }

            final String consume = accept;

            Map<String, String> ppms = null;
            if (mapping.isPathMapping()) {
                ppms = mapping.getPath().compile(request.getPathInfo());
            }

            final Map<String, String> pathParams = ppms;
            Field[] fields = getFields(cls);
            Arrays.stream(fields).forEach(f -> {
                ActionMapping.ParameterMapping param = mapping.getParamMapping(f.getName());
                if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
                    // do nothing

                } else if (param == null && f.getAnnotation(RestActionParameter.class) != null) {
                    RestActionParameter annotation = f.getAnnotation(RestActionParameter.class);
                    param = new ActionMapping.ParameterMapping(annotation.name(),
                            annotation.paramType(),
                            annotation.referredTo().isEmpty() ? annotation.name() : annotation.referredTo(),
                            annotation.description());
                }

                if (param != null) {
                    Class<?> fieldType = f.getType();
                    f.setAccessible(true);
                    if (ParamType.WIRED_PROPERTY.equals(param.getParameterType())) {
                        String value = context.getWiredProperty(param.getReferredTo());
                        try {
                            f.set(callable, ConvertUtils.convert(value, fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.WIRED_SERVICE.equals(param.getParameterType())) {
                        try {
                            f.set(callable, context.getWiredService(param.getReferredTo(), fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.WIRED_RESOURCE.equals(param.getParameterType())) {
                        try {
                            f.set(callable, context.getResource(param.getReferredTo(), fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.PAYLOAD.equals(param.getParameterType())) {
                        try {
                            f.set(callable, getBody(request, fieldType, consume));

                        } catch (IllegalAccessException | IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (ParamType.PATH_PARAM.equals(param.getParameterType())) {
                        if (mapping.isPathMapping()) {
                            String value = pathParams.get(param.getReferredTo());
                            try {
                                f.set(callable, convert(value, fieldType));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } else {
                        String value = getParamValue(request, param.getReferredTo(), param.getParameterType());
                        try {
                            f.set(callable, convert(value, fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            if (DispatchAction.class.isAssignableFrom(cls)) {
                DispatchAction action = (DispatchAction) callable;
                Arrays.stream(action.getPropertyNames()).forEach(prop -> {
                    Class<?> propType = action.getPropertyType(prop);
                    ActionMapping.ParameterMapping parameterMapping = mapping.getParamMapping(prop);
                    if (parameterMapping != null) {
                        if (ParamType.WIRED_PROPERTY.equals(parameterMapping.getParameterType())) {
                            String value = context.getWiredProperty(parameterMapping.getReferredTo());
                            action.setProperty(prop, ConvertUtils.convert(value, propType));

                        } else if (ParamType.WIRED_SERVICE.equals(parameterMapping.getParameterType())) {
                            action.setProperty(prop, context.getWiredService(parameterMapping.getReferredTo(), propType));

                        } else if (ParamType.WIRED_RESOURCE.equals(parameterMapping.getParameterType())) {
                            action.setProperty(prop, context.getResource(parameterMapping.getReferredTo(), propType));

                        } else if (ParamType.PAYLOAD.equals(parameterMapping.getParameterType())) {
                            try {
                                action.setProperty(prop, getBody(request, propType, consume));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        } else {
                            String value = getParamValue(request, parameterMapping.getReferredTo(), parameterMapping.getParameterType());
                            action.setProperty(prop, convert(value, propType));
                        }
                    }
                });
            }

            return callable;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String actionPath(HttpServletRequest req) {
        String path = req.getRequestURI();
        if (!path.equals(req.getServletPath())) {
            path = path.substring(req.getServletPath().length() + 1);

        } else {
            path = "xxx";

        }
        return path;
    }

    private Field[] getFields(Class<?> cls) {
        if (cls.getSuperclass().equals(Object.class)) {
            return cls.getDeclaredFields();
        }

        Map<String, Field> fieldMap = new LinkedHashMap<>();
        Class<?> parent = cls;
        while (!parent.equals(Object.class)) {
            Arrays.stream(parent.getDeclaredFields()).forEach(e -> {
                if (!Modifier.isStatic(e.getModifiers())
                        && !Modifier.isFinal(e.getModifiers())
                        && !fieldMap.containsKey(e.getName())) {
                    fieldMap.put(e.getName(), e);

                }
            });
            parent = parent.getSuperclass();
        }
        return fieldMap.values().toArray(new Field[fieldMap.size()]);
    }

    private String getParamValue(HttpServletRequest request, String param, ParamType paramType) {
        String value = null;
        if (ParamType.HEADER_PARAM.equals(paramType)) {
            value = request.getHeader(param);

        } else if (ParamType.QUERY_PARAM.equals(paramType)) {
            value = request.getParameter(param);

        } else if (ParamType.QUERY_PARAM.equals(paramType)) {
            value = request.getParameter(param);

        }
        if (ParamType.PATH_PARAM.equals(paramType)) {
            System.out.println("=================== path param: " + request.getPathInfo());

        }

        return value;
    }

    private <T> T convert(String value, Class<T> type) {
        return (T) ConvertUtils.convert(value, type);
    }

    private <T> T convert(String value, Class<T> type, String mediaType) {
        logger.warning("TODO: convert");
        return (T) value;
    }

    private <T> T getBody(HttpServletRequest request, Class<T> type, String mediaType) throws IOException {
        return context.getSerializer(mediaType)
                .deserialize(IOUtils.toByteArray(request.getInputStream()), type);
    }

    private void forward(String path, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream outputStream = resp.getOutputStream();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/restruts/" + path);
        byte[] buf = new byte[8192];
        int length;
        while ((length = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, length);
        }

        outputStream.flush();
    }

}
