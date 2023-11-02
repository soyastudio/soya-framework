package soya.framework.spring.actions;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;

@RestAction(
        method = HttpMethod.GET,
        path = "/hello/{id}",
        parameters = {
                @RestActionParameter(
                        name = "id",
                        paramType = ParamType.PATH_PARAM,
                        referredTo = "id"
                ),
                @RestActionParameter(
                        name = "time",
                        paramType = ParamType.QUERY_PARAM,
                        referredTo = "t"
                )
        }
)
public class HelloAction extends AbstractAction<String> {

    private String id;

    private Long time;

    @Override
    public String call() throws Exception {
        System.out.println("============ message: " + time);
        return "Hello World";
    }
}
