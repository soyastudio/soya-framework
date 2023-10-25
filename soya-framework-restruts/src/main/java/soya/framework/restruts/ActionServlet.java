package soya.framework.restruts;

import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;

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

    public ActionServlet() {
        super();
        Arrays.stream(getClass().getDeclaredMethods()).forEach(method -> {
            RestAction restMapping = method.getAnnotation(RestAction.class);
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

        ServiceLocator locator = ServiceLocatorSingleton.getInstance();
        System.out.println("=================== " + locator);

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

}
