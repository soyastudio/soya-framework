package soya.framework.restruts;

import java.io.IOException;

public interface RestApiRenderer {
    String render(RestActionContext registration) throws IOException;
}
