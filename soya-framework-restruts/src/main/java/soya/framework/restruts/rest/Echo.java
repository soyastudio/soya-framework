package soya.framework.restruts.rest;

import soya.framework.restruts.RestAction;
import soya.framework.restruts.HttpMethod;

@RestAction(
        action = "soya.framework.restruts.actions.EchoAction",
        method = HttpMethod.POST,
        path = "/echo"
)
public interface Echo {
}
