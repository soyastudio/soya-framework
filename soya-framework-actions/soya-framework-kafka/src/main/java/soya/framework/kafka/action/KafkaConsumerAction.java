package soya.framework.kafka.action;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public abstract class KafkaConsumerAction<T> extends KafkaAction<T> {
    protected KafkaConsumer kafkaConsumer() {
        return getKafkaClient().consumer();
    }
}
