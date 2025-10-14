package seedu.address.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link CommandHistory}.
 */
public class CommandHistoryTest {

    private CommandHistory history;

    @BeforeEach
    public void setUp() {
        history = new CommandHistory();
    }

    @Test
    public void add_singleCommand_success() {
        history.add("list");
        assertEquals("list", history.getPrevious());
    }

    @Test
    public void getPrevious_multipleCommands_navigatesCorrectly() {
        history.add("list");
        history.add("help");
        history.add("exit");

        // Move backward through history
        assertEquals("exit", history.getPrevious());
        assertEquals("help", history.getPrevious());
        assertEquals("list", history.getPrevious());

        // Should remain at oldest command
        assertNull(history.getPrevious());
    }

    @Test
    public void getNext_multipleCommands_navigatesCorrectly() {
        history.add("a");
        history.add("b");
        history.add("c");

        // Move to the start
        history.getPrevious(); // "c"
        history.getPrevious(); // "b"

        // Now go forward again
        assertEquals("c", history.getNext());
        assertEquals("", history.getNext()); // end of history → blank input
    }

    @Test
    public void emptyHistory_returnsNullOrEmptyString() {
        assertNull(history.getPrevious());
        assertEquals("", history.getNext());
    }

    @Test
    public void add_emptyOrNullCommand_ignored() {
        history.add("");
        history.add("  ");
        history.add(null);
        assertNull(history.getPrevious());
    }

    @Test
    public void clear_removesAllHistory() {
        history.add("clear");
        history.add("exit");
        history.clear();

        assertNull(history.getPrevious());
        assertEquals("", history.getNext());
    }
}
