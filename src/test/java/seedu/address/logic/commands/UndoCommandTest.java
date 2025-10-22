package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code UndoCommand}.
 */
public class UndoCommandTest {

    private Model model;
    private final Person alice = new PersonBuilder().withName("alice").build();
    private final Person bob = new PersonBuilder().withName("bob").build();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_noPreviousState_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();
        assertThrows(CommandException.class, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_singleUndo_success() throws Exception {
        // Save current state
        model.saveState();

        // Perform change
        model.addPerson(alice);
        assertTrue(model.hasPerson(alice));

        // Undo
        UndoCommand undoCommand = new UndoCommand();
        CommandResult result = undoCommand.execute(model);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertFalse(model.hasPerson(alice));
    }

    @Test
    public void execute_multipleUndo_success() throws Exception {
        // Initial state
        model.saveState();
        model.addPerson(alice);

        // Save and modify again
        model.saveState();
        model.addPerson(bob);
        assertTrue(model.hasPerson(bob));

        UndoCommand undoCommand = new UndoCommand();

        // Undo bob addition
        undoCommand.execute(model);
        assertFalse(model.hasPerson(bob));

        // Undo alice addition
        undoCommand.execute(model);
        assertFalse(model.hasPerson(alice));
    }

    @Test
    public void execute_undoBeyondAvailableStates_throwsCommandException() throws Exception {
        model.saveState();
        model.addPerson(alice);

        UndoCommand undoCommand = new UndoCommand();

        // First undo succeeds
        undoCommand.execute(model);

        // Second undo should fail
        assertThrows(CommandException.class, () -> undoCommand.execute(model));
    }
}
