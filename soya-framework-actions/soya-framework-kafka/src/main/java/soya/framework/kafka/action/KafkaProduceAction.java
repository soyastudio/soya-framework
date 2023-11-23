package soya.framework.kafka.action;

import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;


@ActionDefinition(domain = "kafka",
        name = "produce")
public class KafkaProduceAction extends KafkaProducerAction<RecordMetadata> {

    @ActionParameter(type = ActionParameterType.PROPERTY, required = true)
    protected String topic;

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected Integer partition;

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected String keySerializer;

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected String valueSerializer;

    @ActionParameter(type = ActionParameterType.INPUT,
            description = "Message for produce",
            required = true)
    protected String message;

    @Override
    public RecordMetadata call() throws Exception {
        return send(producer(), createProducerRecord(topic, partition, null, message, null), 30000l);
    }
}
