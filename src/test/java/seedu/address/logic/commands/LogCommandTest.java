package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.interaction.Interaction;
import seedu.address.model.interaction.InteractionType;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

public class LogCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        AddressBook ab = new AddressBook();

        Person p1 = new Person(
                new Name("Alice"), new Phone("81234567"), new Email("alice@example.com"),
                new Address("1 Kent Ridge Rd"), new HashSet<>(), List.of());
        Person p2 = new Person(
                new Name("Bob"), new Phone("91234567"), new Email("bob@example.com"),
                new Address("2 Computing Dr"), new HashSet<>(), List.of());

        ab.addPerson(p1);
        ab.addPerson(p2);

        model = new ModelManager(ab, new UserPrefs());
    }

    @Test
    public void execute_appendInteraction_success() throws Exception {
        Index idx = Index.fromOneBased(1);
        Person before = model.getFilteredPersonList().get(idx.getZeroBased());

        LogCommand cmd = new LogCommand(idx, InteractionType.CALL, "Followed up");
        CommandResult result = cmd.execute(model);

        Person after = model.getFilteredPersonList().get(idx.getZeroBased());

        String expectedMsgStart = String.format(LogCommand.MESSAGE_SUCCESS, InteractionType.CALL, before.getName());
        assertTrue(result.getFeedbackToUser().startsWith(expectedMsgStart));

        List<Interaction> history = after.getInteractions();
        org.junit.jupiter.api.Assertions.assertFalse(history.isEmpty());
        assertEquals("Followed up", history.get(history.size() - 1).getDetails());

        assertEquals(before.getName(), after.getName());
        assertEquals(before.getEmail(), after.getEmail());
        assertEquals(before.getPhone(), after.getPhone());
    }

    @Test
    public void execute_invalidIndex_throws() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LogCommand cmd = new LogCommand(outOfBounds, InteractionType.NOTE, "nope");
        assertThrows(CommandException.class, () -> cmd.execute(model),
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
