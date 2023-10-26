package soya.framework.restruts;

import soya.framework.restruts.api.Swagger;
import soya.framework.restruts.api.SwaggerRenderer;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ActionServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ActionServlet.class.getName());
    protected Map<String, Method> getMethods = new HashMap<>();
    protected Map<String, Method> postMethods = new HashMap<>();
    protected Map<String, Method> putMethods = new HashMap<>();

    protected Map<String, Method> deleteMethods = new HashMap<>();

    private ServletRegistration registration;

    private RestActionRegistration restActionRegistration = new RestActionRegistration();
    private RestApiRenderer renderer = new SwaggerRenderer();

    public ActionServlet() {
        super();
        Arrays.stream(getClass().getDeclaredMethods()).forEach(method -> {
            RestActionMapping restMapping = method.getAnnotation(RestActionMapping.class);
            if (restMapping != null) {
                if (HttpMethod.GET.equals(restMapping.method())) {
                    getMethods.put(restMapping.path(), method);

                } else if (HttpMethod.POST.equals(restMapping.method())) {
                    postMethods.put(restMapping.path(), method);

                } else if (HttpMethod.PUT.equals(restMapping.method())) {
                    putMethods.put(restMapping.path(), method);

                } else if (HttpMethod.DELETE.equals(restMapping.method())) {
                    deleteMethods.put(restMapping.path(), method);

                }
            }
        });

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.registration = config.getServletContext().getServletRegistration(getServletName());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = actionPath(req);
        if (path != null && (path.endsWith(".png")
                || path.endsWith(".html")
                || path.endsWith(".json")
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
        AsyncContext asyncContext = req.startAsync();
        asyncContext.start(new RestActionTask(asyncContext));
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

    private String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    private void forward(String path, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream outputStream = resp.getOutputStream();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/gherkin/" + path);
        byte[] buf = new byte[8192];
        int length;
        while ((length = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, length);
        }

        outputStream.flush();
    }

/*

    protected void initSwagger(ActionMappings mappings) {
        logger.info("initializing swagger");

        Swagger.SwaggerBuilder builder = Swagger.builder();
        String path = this.getServletInfo();
        for (String e : registration.getMappings()) {
            if (e.endsWith("/*")) {
                path = e.substring(0, e.lastIndexOf("/*"));
            }
        }
        builder.basePath(path);

        mappings.domains().forEach(dm -> {
            builder.addTag(Swagger.TagObject.instance()
                    .name(dm.getTitle().isEmpty() ? dm.getName() : dm.getTitle())
                    .description(dm.getDescription()));

            dm.getActionMappings().forEach(am -> {
                ActionName actionName = am.getActionName();
                String httpMethod = am.getHttpMethod().toUpperCase();

                String fullPath = dm.getPath() + am.getPath();

                String operationId = dm.getName().replaceAll("_", "-")
                        + "_"
                        + actionName.getName().replaceAll("_", "-");

                Swagger.PathBuilder pathBuilder = null;
                if (httpMethod.equals("GET")) {
                    pathBuilder = builder.get(fullPath, operationId);

                } else if (httpMethod.equals("POST")) {
                    pathBuilder = builder.post(fullPath, operationId);

                } else if (httpMethod.equals("DELETE")) {
                    pathBuilder = builder.delete(fullPath, operationId);

                } else if (httpMethod.equals("PUT")) {
                    pathBuilder = builder.put(fullPath, operationId);

                } else if (httpMethod.equals("HEAD")) {
                    pathBuilder = builder.head(fullPath, operationId);

                } else if (httpMethod.equals("OPTIONS")) {
                    pathBuilder = builder.options(fullPath, operationId);

                } else if (httpMethod.equals("PATCH")) {
                    pathBuilder = builder.patch(fullPath, operationId);

                }

                pathBuilder.description(am.getDescription());
                pathBuilder.addTag(dm.getTitle() != null && !dm.getTitle().isEmpty() ? dm.getTitle() : dm.getName());
                pathBuilder.produces(am.getProduce());

                if (pathBuilder != null) {
                    for (ParameterMapping pm : am.getParameters()) {
                        String name = pm.getName();
                        ActionParameterType paramType = pm.getParameterType();

                        if (ActionParameterType.PATH_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "path", pm.getDescription()).build();

                        } else if (ActionParameterType.QUERY_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "query", pm.getDescription()).build();

                        } else if (ActionParameterType.HEADER_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "header", pm.getDescription()).build();

                        } else if (ActionParameterType.COOKIE_PARAM.equals(paramType)) {
                            pathBuilder.parameterBuilder(name, "cookie", pm.getDescription()).build();

                        } else if (ActionParameterType.PAYLOAD.equals(paramType)) {
                            pathBuilder.bodyParameterBuilder(name, pm.getDescription())
                                    .build()
                                    .consumes(pm.getContentType());
                        }
                    }
                }
                pathBuilder.build();
            });
        });

        this.swagger = builder.build();
    }
*/


}
