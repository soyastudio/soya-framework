package soya.framework.kafka.action;

import org.apache.kafka.clients.admin.AdminClient;

public abstract class KafkaAdminAction<T> extends KafkaAction<T> {

    protected AdminClient adminClient() {
        return getKafkaClient().adminClient();
    }
}
