package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestAction(
        id = "runtime-threads",
        path = "/restruts/runtime/threads",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct"
)
public class RuntimeThreadsAction extends ReflectAction {
    @Override
    public Object call() throws Exception {
        List<ThreadModel> list = new ArrayList<>();
        Thread.getAllStackTraces().keySet().forEach(e -> {
            list.add(new ThreadModel(e));
        });

        Collections.sort(list);
        return GSON.toJson(list);
    }

    static class ThreadModel implements Comparable<ThreadModel> {
        private String name;
        private String type;
        private int priority;

        private ThreadModel(Thread thread) {
            this.name = thread.getName();
            this.type = thread.getClass().getName();
            this.priority = thread.getPriority();

        }

        @Override
        public int compareTo(ThreadModel o) {
            return this.name.compareTo(o.name);
        }
    }
}
