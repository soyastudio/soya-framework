package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.actions.ServiceDispatchAction;

@RestAction(
        id = "action-domains",
        path = "/restruts/action/domains",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct",
        action = "soya.framework.action.ActionRegistration/domains()"
)
public class ActionDomains extends ServiceDispatchAction<String[]> {
}
