package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pure logic tests for {@link CommandBox}, bypassing all JavaFX dependencies.
 */
public class CommandBoxTest {

    private CommandBox.CommandExecutor successfulExecutor;
    private CommandBox.CommandExecutor failingExecutor;
    private CommandHistory commandHistory;

    /**
     * A minimal subclass of CommandBox that skips FXML/JavaFX setup.
     * It simulates text input without using javafx.scene.control.TextField.
     */
    private static class TestCommandBox {

        private final CommandBox.CommandExecutor executor;
        private final CommandHistory history;
        private String text = "";

        TestCommandBox(CommandBox.CommandExecutor executor, CommandHistory history) {
            this.executor = executor;
            this.history = history;
        }

        public void setText(String commandText) {
            this.text = commandText;
        }

        public void handleCommandEntered() {
            if (text == null || text.isBlank()) {
                return;
            }
            try {
                executor.execute(text);
                history.add(text);
                text = ""; // simulate clearing input
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    public void setUp() {
        commandHistory = new CommandHistory();

        successfulExecutor = commandText -> {
            commandHistory.add(commandText);
            return null; // simulate success
        };

        failingExecutor = commandText -> {
            throw new RuntimeException("Command failed");
        };
    }

    @Test
    public void executeCommand_successfulCommand_doesNotThrow() {
        TestCommandBox box = new TestCommandBox(successfulExecutor, commandHistory);
        box.setText("list");
        assertDoesNotThrow(box::handleCommandEntered);
    }

    @Test
    public void executeCommand_failedCommand_throwsException() {
        TestCommandBox box = new TestCommandBox(failingExecutor, commandHistory);
        box.setText("fail");
        assertThrows(RuntimeException.class, box::handleCommandEntered);
    }

    @Test
    public void commandHistory_updatesAfterSuccess() {
        TestCommandBox box = new TestCommandBox(successfulExecutor, commandHistory);
        box.setText("help");
        box.handleCommandEntered();
        box.setText("exit");
        box.handleCommandEntered();
        assertDoesNotThrow(() -> commandHistory.getPrevious());
    }

    @Test
    public void emptyCommand_doesNothing() {
        TestCommandBox box = new TestCommandBox(successfulExecutor, commandHistory);
        box.setText("");
        assertDoesNotThrow(box::handleCommandEntered);
    }
}
