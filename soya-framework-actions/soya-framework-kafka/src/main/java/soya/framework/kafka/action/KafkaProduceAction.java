package soya.framework.kafka.action;

import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;


@ActionDefinition(domain = "kafka",
        name = "produce")
public class KafkaProduceAction extends KafkaProducerAction<RecordMetadata> {

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE, required = true)
    protected String topic;

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    protected Integer partition;

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    protected String keySerializer;

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    protected String valueSerializer;

    @ActionParameterDefinition(parameterType = ActionParameterType.INPUT,
            description = "Message for produce",
            required = true)
    protected String message;

    @Override
    public RecordMetadata call() throws Exception {
        return send(producer(), createProducerRecord(topic, partition, null, message, null), 30000l);
    }
}
