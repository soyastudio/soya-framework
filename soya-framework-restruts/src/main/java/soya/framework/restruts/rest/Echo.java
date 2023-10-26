package soya.framework.restruts.rest;

import soya.framework.restruts.RestActionMapping;
import soya.framework.restruts.HttpMethod;

@RestActionMapping(
        action = "soya.framework.restruts.actions.EchoAction",
        method = HttpMethod.POST,
        path = "/echo"
)
public interface Echo {
}
