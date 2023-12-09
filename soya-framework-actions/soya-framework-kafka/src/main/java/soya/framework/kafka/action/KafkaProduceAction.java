package soya.framework.kafka.action;

import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;


@ActionDefinition(domain = "kafka",
        name = "produce")
public class KafkaProduceAction extends KafkaProducerAction<RecordMetadata> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM, required = true)
    protected String topic;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    protected Integer partition;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    protected String keySerializer;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    protected String valueSerializer;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT,
            description = "Message for produce",
            required = true)
    protected String message;

    @Override
    public RecordMetadata call() throws Exception {
        return send(producer(), createProducerRecord(topic, partition, null, message, null), 30000l);
    }
}
