package soya.framework.restruts;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.util.ConvertUtils;
import soya.framework.restruts.util.ReflectUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

            final Callable<?> callable = create(actionMapping, req);
            final AsyncContext asyncContext = req.startAsync();

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
                    if(context.getExceptionHandler() != null) {
                        context.getExceptionHandler().handleException(e, resp);

                    } else {
                        throw new RuntimeException(e);
                    }
                } finally {
                    asyncContext.complete();
                }
            });
        }
    }

    private Callable<?> create(ActionMapping mapping, HttpServletRequest request) {

        Class<?> cls = mapping.getActionClass();

        Callable<?> callable = null;
        Constructor[] constructors = cls.getConstructors();
        Constructor constructor = constructors[0];

        if(constructor.getParameters().length == 1
                && constructor.getParameters()[0].getType().equals(ActionMapping.class)) {
            try {
                callable = (Callable<?>) constructor.newInstance(mapping);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                callable = (Callable<?>) cls.newInstance();

            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            final Callable<?> task = callable;
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
            Field[] fields = ReflectUtils.getFields(cls);
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
                        String value = context.getProperty(param.getReferredTo());
                        try {
                            f.set(task, ConvertUtils.convert(value, fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.WIRED_SERVICE.equals(param.getParameterType())) {
                        try {
                            f.set(task, context.getService(param.getReferredTo(), fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.WIRED_RESOURCE.equals(param.getParameterType())) {
                        try {
                            f.set(task, context.getResource(param.getReferredTo(), fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ParamType.PAYLOAD.equals(param.getParameterType())) {
                        try {
                            f.set(task, getBody(request, fieldType, consume));

                        } catch (IllegalAccessException | IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (ParamType.PATH_PARAM.equals(param.getParameterType())) {
                        if (mapping.isPathMapping()) {
                            String value = pathParams.get(param.getReferredTo());
                            try {
                                f.set(task, convert(value, fieldType));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } else {
                        String value = getParamValue(request, param.getReferredTo(), param.getParameterType());
                        try {
                            f.set(task, convert(value, fieldType));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            if (DispatchAction.class.isAssignableFrom(cls)) {
                DispatchAction action = (DispatchAction) task;

                Arrays.stream(action.getPropertyNames()).forEach(prop -> {
                    Class<?> propType = action.getPropertyType(prop);
                    ActionMapping.ParameterMapping parameterMapping = mapping.getParamMapping(prop);

                    if (parameterMapping != null) {
                        if (ParamType.WIRED_PROPERTY.equals(parameterMapping.getParameterType())) {
                            String value = context.getProperty(parameterMapping.getReferredTo());
                            action.setProperty(prop, ConvertUtils.convert(value, propType));

                        } else if (ParamType.WIRED_SERVICE.equals(parameterMapping.getParameterType())) {
                            action.setProperty(prop, context.getService(parameterMapping.getReferredTo(), propType));

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

            return task;

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
