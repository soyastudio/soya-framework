package soya.framework.commons.evaluation;

public interface Evaluator {

    Object evaluate(String exp, Object session) throws EvaluationException;

}
