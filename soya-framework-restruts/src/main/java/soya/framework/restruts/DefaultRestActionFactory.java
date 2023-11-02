package soya.framework.restruts;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class DefaultRestActionFactory implements RestActionFactory {

    public static final String namespace = "class:";

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public Callable<?> create(ActionMapping mapping) throws ActionCreationException {
        Callable<?> callable = null;
        if(mapping.getActionClass() != null) {
            try {
                callable = (Callable<?>) mapping.getActionClass().newInstance();

            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return callable;
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
