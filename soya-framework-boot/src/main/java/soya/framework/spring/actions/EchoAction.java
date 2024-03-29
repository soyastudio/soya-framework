package soya.framework.spring.actions;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;
import soya.framework.spring.service.DemoServiceImpl;

import java.util.concurrent.Callable;

@RestAction(
        method = HttpMethod.POST,
        path = "/echo",
        parameters = {
                @RestActionParameter(
                        name = "message",
                        paramType = ParamType.PAYLOAD
                ),
                @RestActionParameter(
                        name = "service",
                        paramType = ParamType.WIRED_SERVICE,
                        referredTo = "soya.framework.spring.service.DemoService"
                )
        },
        tags = "demo"
)
public class EchoAction implements Callable<String> {

    private String message;

    private DemoServiceImpl service;

    @Override
    public String call() throws Exception {
        System.out.println("================== " + service);

        return message;
    }
}
