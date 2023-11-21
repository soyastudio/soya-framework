package soya.framework.kafka.action;

import org.apache.kafka.clients.producer.KafkaProducer;

public abstract class KafkaProducerAction<T> extends KafkaAction<T>{

    protected KafkaProducer producer() {
        return getKafkaClient().producer();
    }
}
