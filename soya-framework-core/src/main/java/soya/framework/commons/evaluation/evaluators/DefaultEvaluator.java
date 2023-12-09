package soya.framework.commons.evaluation.evaluators;

import soya.framework.commons.evaluation.EvaluationException;
import soya.framework.commons.evaluation.Evaluator;

public class DefaultEvaluator implements Evaluator {
    private static DefaultEvaluator me;

    static {
        me = new DefaultEvaluator();
    }

    private DefaultEvaluator() {
    }

    public static Evaluator getInstance() {
        return me;
    }

    @Override
    public Object evaluate(String exp, Object session) throws EvaluationException {
        // TODO:
        return null;
    }
}
