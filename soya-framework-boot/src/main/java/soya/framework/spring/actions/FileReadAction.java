package soya.framework.spring.actions;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

@RestAction(
        method = HttpMethod.GET,
        path = "/file",
        parameters = {
                @RestActionParameter(name = "file",
                        paramType = ParamType.WIRED_RESOURCE,
                        referredTo = "file:D:/github/soya-framework/LICENSE"
                )
        }
)
public class FileReadAction implements Callable<String> {

    private File file;

    @Override
    public String call() throws Exception {
        return IOUtils.toString(new FileInputStream(file));
    }
}
