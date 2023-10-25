package soya.framework.restruts;

import soya.framework.restruts.actions.EchoAction;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.Callable;

public class RestActionTask implements Runnable {
    private final AsyncContext asyncContext;

    public RestActionTask(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        try {
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            OutputStream out = response.getOutputStream();

            out.write(getBody((HttpServletRequest) asyncContext.getRequest()).getBytes());

            Callable<?> action = create((HttpServletRequest) asyncContext.getRequest(), EchoAction.class);
            Object result = action.call();

            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            asyncContext.complete();

        }
    }

    protected Callable create(HttpServletRequest request, Class<? extends Callable> actionType) throws Exception {
        RestAction mapping = actionType.getAnnotation(RestAction.class);
        if (mapping != null) {

        } else {

        }

        EchoAction action = new EchoAction();

        return action;
    }

    protected String getBody(HttpServletRequest request) throws IOException {

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
}
