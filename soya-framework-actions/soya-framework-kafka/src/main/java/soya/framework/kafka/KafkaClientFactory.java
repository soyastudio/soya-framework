package soya.framework.kafka;

public interface KafkaClientFactory {
    KafkaClient getKafkaClient(String name);
}
