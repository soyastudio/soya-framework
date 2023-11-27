package soya.framework.kafka.action;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;
import soya.framework.kafka.KafkaClient;
import soya.framework.kafka.KafkaClientFactory;

import java.util.concurrent.Callable;

public abstract class KafkaAction<T> implements Callable<T> {

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    protected String kafkaClientName;

    protected KafkaClient getKafkaClient() {
        ServiceLocator serviceLocator = ServiceLocatorSingleton.getInstance();
        KafkaClient kafkaClient = null;
        if (kafkaClientName != null && !kafkaClientName.isEmpty()) {
            try {
                kafkaClient = serviceLocator.getService(kafkaClientName, KafkaClient.class);

            } catch (ServiceLocateException e) {

            }

            try {
                KafkaClientFactory factory = serviceLocator.getService(KafkaClientFactory.class);
                kafkaClient = factory.getKafkaClient(kafkaClientName);

            } catch (ServiceLocateException e) {

            }

        } else {
            kafkaClient = serviceLocator.getService(KafkaClient.class);
        }

        if(kafkaClient == null) {
            throw new ServiceLocateException("Cannot find kafka client.");
        }

        return kafkaClient;
    }
}
