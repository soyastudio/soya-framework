package soya.framework.commons.mocking;

public interface Mocker {
    <T> T getMockValue(Class<T> type);
}
