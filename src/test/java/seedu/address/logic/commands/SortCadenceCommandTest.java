package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class SortCadenceCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortCadence_success() {
        SortCadenceCommand command = new SortCadenceCommand();

        CommandResult result = command.execute(model);
        command.execute(expectedModel);

        assertEquals(SortCadenceCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        assertEquals(model.getFilteredPersonList(), expectedModel.getFilteredPersonList());
    }
}
