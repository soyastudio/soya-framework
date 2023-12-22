package soya.framework.action.orchestration.dsl;

public interface Keywords {

    enum Activity {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        TRANSFORM,
        PROCESS,
        SEND
    }

    enum Preposition {
        AS,
        FROM,
        TO,
        FOR,
        ON,
        THROUGH
    }
}
