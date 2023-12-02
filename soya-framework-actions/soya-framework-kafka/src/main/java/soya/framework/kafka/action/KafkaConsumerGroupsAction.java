package soya.framework.kafka.action;

import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.TopicPartition;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ActionDefinition(domain = "kafka",
        name = "admin-consumer-groups")
public class KafkaConsumerGroupsAction extends KafkaAdminAction<Metric[]> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE, required = true)
    private String topicName;

    @Override
    public Metric[] call() throws Exception {

        List<String> groupIds = adminClient().listConsumerGroups().all().get().
                stream().map(s -> s.groupId()).collect(Collectors.toList());

        Map<String, ConsumerGroupDescription> groups = adminClient().
                describeConsumerGroups(groupIds).all().get();

        for (final String groupId : groupIds) {
            ConsumerGroupDescription desc = groups.get(groupId);
            //find if any description is connected to the topic with topicName
            Optional<TopicPartition> tp = desc.members().stream().
                    map(s -> s.assignment().topicPartitions()).
                    flatMap(coll -> coll.stream()).
                    filter(s -> s.topic().equals(topicName)).findAny();

            if (tp.isPresent()) {
                //you found the consumer, so collect the group id somewhere
                System.out.println(groupId);
            }
        }

        return null;
    }
}
