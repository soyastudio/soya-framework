package soya.framework.action.flow;

import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionExecutor;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class PipelineExecutor<T> {

    private ActionContext actionContext;
    private Session session;
    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    PipelineExecutor(Session session, List<Task> tasks, ActionContext actionContext) {
        this.actionContext = actionContext;
        this.session = session;
        tasks.addAll(tasks);
    }

    public T execute() throws Exception {
        return create().call();
    }

    public Future<T> submit(ExecutorService executorService) throws Exception {

        return (Future<T>) executorService.submit(create());
    }

    private Callable<T> create() {
        return null;
    }

    static class Worker implements Callable {
        private Task task;
        private Session session;
        private ActionContext actionContext;

        @Override
        public Object call() throws Exception {
            ActionExecutor actionExecutor = ActionClass.forName(task.getActionName()).newInstance(actionContext);

            return null;
        }
    }






}
