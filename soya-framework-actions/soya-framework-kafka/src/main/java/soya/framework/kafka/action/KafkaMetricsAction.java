package soya.framework.kafka.action;

import org.apache.kafka.common.Metric;
import soya.framework.action.ActionDefinition;

import java.util.Collection;

    @ActionDefinition(domain = "kafka",
            name = "admin-metrics")
public class KafkaMetricsAction extends KafkaAdminAction<Metric[]> {
    @Override
    public Metric[] call() throws Exception {
        Collection<? extends Metric> results = adminClient().metrics().values();
        return results.toArray(new Metric[results.size()]);
    }
}
