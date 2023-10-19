package soya.framework.commons.cli;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTest {
    static Logger log = Logger.getLogger(CommandTest.class.getName());

    @BeforeAll
    static void setup() {
        log.info("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
    }

    @AfterEach
    void tearDown() {
        log.info("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        log.info("@AfterAll - executed after all test methods.");
    }

    @Test
    public void testCommandBuilder() {
        Command command = Command.builder("CMD").create();

        assertEquals("CMD", command.getCommand());
    }

}
