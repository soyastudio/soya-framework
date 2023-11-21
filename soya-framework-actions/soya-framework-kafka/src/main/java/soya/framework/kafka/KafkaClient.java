package soya.framework.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public interface KafkaClient {
    AdminClient adminClient();

    KafkaProducer producer();

    <k, v> KafkaProducer<k, v> producer(Serializer<k> keySerializer, Serializer<v> valueSerializer);

    KafkaConsumer consumer();

    <k, v> KafkaConsumer<k, v> consumer(Deserializer<k> keyDeserializer, Deserializer<v> valueDeserializer);
}
