package soya.framework.action.actions.mock;

import soya.framework.action.ActionDefinition;

import java.util.Random;

@ActionDefinition(
        domain = "mock",
        name = "random"
)
public class MockRandomAction extends AbstractMockAction {

    @Override
    protected Object getMockResult(Class<?> expectedType) {

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 128;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
