package soya.framework.restruts.rest;

import soya.framework.restruts.*;

@RestActionDomain(actions = {
        @RestAction(
                action = "",
                method = HttpMethod.POST,
                path = "",
                parameters = {
                        @RestActionParameter(name = "message", httpParam = HttpParam.PAYLOAD)
                },
                consumes = "",
                produces = ""
        )
})
public interface RestService {
}
