package soya.framework.gherkin;

import java.io.IOException;
import java.io.InputStream;

public class FeatureParser {
    private FeatureParser() {
    }

    public static <T> T parse(InputStream inputStream, FeatureVisitor<T> visitor) throws IOException {
        visitor.visit(inputStream);
        return visitor.getResult();
    }
}
