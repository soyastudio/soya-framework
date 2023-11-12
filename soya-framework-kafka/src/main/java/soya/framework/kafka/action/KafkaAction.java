package soya.framework.kafka.action;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;
import soya.framework.kafka.KafkaClient;

import java.util.concurrent.Callable;

public abstract class KafkaAction<T> implements Callable<T> {

    @ActionParameter(type = ActionParameterType.WIRED_SERVICE)
    protected KafkaClient kafkaClient;
}
