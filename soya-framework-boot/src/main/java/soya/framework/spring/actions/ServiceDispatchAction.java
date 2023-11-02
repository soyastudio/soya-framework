package soya.framework.spring.actions;

import soya.framework.restruts.*;

@RestAction(
        action = "bean://soya.framework.spring.service.DemoService/hello(String, String)",
        method = HttpMethod.GET,
        path = "/dispatch"
)
public class ServiceDispatchAction extends DispatchAction<String> {

}
