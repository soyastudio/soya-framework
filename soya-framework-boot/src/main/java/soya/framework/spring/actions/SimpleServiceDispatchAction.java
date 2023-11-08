package soya.framework.spring.actions;

import soya.framework.restruts.*;
import soya.framework.restruts.actions.ServiceDispatchAction;

@RestAction(
        id = "dispatch",
        action = "demo/test(String, String)?0=pa&1=pb",
        path = "/dispatch",
        method = HttpMethod.GET,
        parameters = {
                @RestActionParameter(
                        name = "pa",
                        paramType = ParamType.HEADER_PARAM,
                        referredTo = "parameter_A"
                ),
                @RestActionParameter(
                        name = "pb",
                        paramType = ParamType.WIRED_PROPERTY,
                        referredTo = "soya.framework.name"
                )
        }
)
public class SimpleServiceDispatchAction extends ServiceDispatchAction<String> {

}
