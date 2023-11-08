package soya.framework.spring.actions;

import soya.framework.restruts.*;
import soya.framework.restruts.actions.CallableDispatchAction;

@RestAction(
        id = "callable-dispatch",
        action = "soya.framework.spring.actions.MyCallable",
        path = "/dispatch/callable",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(
                        name = "message",
                        paramType = ParamType.WIRED_RESOURCE,
                        referredTo = "classpath:banner.txt"
                )
        }
)
public class MyCallableDispatcher extends CallableDispatchAction<String> {
}
