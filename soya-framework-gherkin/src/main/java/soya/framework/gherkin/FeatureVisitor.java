package soya.framework.gherkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class FeatureVisitor<T> implements GherkinKeywords {

    protected FeatureVisitor() {
    }

    void visit(InputStream inputStream) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String ln = line.trim();

                if (ln.startsWith(GherkinKeywords.COMMENT)) {
                    ln = ln.substring(COMMENT.length()).trim();
                    visitComment(ln);

                } else if (ln.startsWith(FEATURE)) {
                    ln = ln.substring(FEATURE.length()).trim();
                    visitFeature(ln);

                } else if (ln.equals(BACKGROUND)) {
                    visitBackground();

                } else if (ln.startsWith(SCENARIO)) {
                    ln = ln.substring(SCENARIO.length()).trim();
                    visitScenario(ln);

                } else if (ln.startsWith(GIVEN)) {
                    ln = ln.substring(GIVEN.length()).trim();
                    visitGiven(ln);

                } else if (ln.startsWith(WHEN)) {
                    ln = ln.substring(WHEN.length()).trim();
                    visitWhen(ln);

                } else if (ln.startsWith(THEN)) {
                    ln = ln.substring(THEN.length()).trim();
                    visitThen(ln);

                } else if (ln.startsWith(AND)) {
                    ln = ln.substring(AND.length()).trim();
                    visitAnd(ln);

                } else if (ln.startsWith(SCENARIO_OUTLINE)) {
                    visitScenarioOutline(ln);

                } else if (ln.startsWith(EXAMPLES)) {
                    visitExamples();

                } else if (ln.startsWith(TABLE_SEPARATOR) && ln.endsWith(TABLE_SEPARATOR)) {
                    visitDataLine(ln);

                } else {
                    visitTextLine(line);
                }
            }
        } catch (IOException e) {
            throw e;
        }

        visitEnd();
    }

    protected abstract T getResult();

    protected abstract void visitFeature(String line);

    protected abstract void visitBackground();

    protected abstract void visitScenarioOutline(String line);

    protected abstract void visitScenario(String line);

    protected abstract void visitGiven(String line);

    protected abstract void visitWhen(String line);

    protected abstract void visitThen(String line);

    protected abstract void visitAnd(String line);

    protected abstract void visitExample(String line);

    protected abstract void visitExamples();

    protected abstract void visitDataLine(String line);

    protected abstract void visitTextLine(String line);

    protected abstract void visitComment(String line);

    protected abstract void visitEnd();
}
