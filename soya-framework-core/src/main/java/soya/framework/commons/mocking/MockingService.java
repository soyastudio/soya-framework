package soya.framework.commons.mocking;

public abstract class MockingService {
    public static final String DEFAULT_VALUE_STRATEGY = "DEFAULT_VALUE";

    private static MockingService me;


    protected MockingService() {
        if(me != null) {
            throw new IllegalStateException("");
        }
    }

    public static MockingService getInstance() {
        if(me == null) {
            throw new IllegalStateException("");
        }

        return me;
    }

    public <T> T getMockResult(Class<T> type) {
        return null;
    }
}
