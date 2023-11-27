package soya.framework.kafka.action;

import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;


@ActionDefinition(domain = "kafka",
        name = "produce")
public class KafkaProduceAction extends KafkaProducerAction<RecordMetadata> {

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY, required = true)
    protected String topic;

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    protected Integer partition;

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    protected String keySerializer;

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    protected String valueSerializer;

    @ActionParameterDefinition(type = ActionParameterType.INPUT,
            description = "Message for produce",
            required = true)
    protected String message;

    @Override
    public RecordMetadata call() throws Exception {
        return send(producer(), createProducerRecord(topic, partition, null, message, null), 30000l);
    }
}
