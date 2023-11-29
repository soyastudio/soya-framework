package soya.framework.kafka.action;

import org.apache.kafka.clients.admin.AdminClient;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@ActionDefinition(domain = "kafka",
        name = "admin-topics")
public class KafkaTopicsAction extends KafkaAdminAction<String[]> {

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    private String query;

    @Override
    public String[] call() throws Exception {
        List<String> topics = topics(adminClient());
        if (query != null) {
            List<String> filtered = filter(query, topics);
            return filtered.toArray(new String[filtered.size()]);

        } else {
            return topics.toArray(new String[topics.size()]);

        }
    }


    private List<String> filter(String query, List<String> results) {
        List<String> filtered = new ArrayList<>();
        results.forEach(e -> {
            if (match(e, query)) {
                filtered.add(e);
            }
        });
        return filtered;
    }

    private boolean match(String value, String patten) {
        return value.startsWith(patten);
    }

    private List<String> topics(AdminClient adminClient) {
        Future<Set<String>> future = adminClient.listTopics().names();
        while (!future.isDone()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            List<String> results = new ArrayList<>(future.get());
            Collections.sort(results);

            return results;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);

        }
    }
}
