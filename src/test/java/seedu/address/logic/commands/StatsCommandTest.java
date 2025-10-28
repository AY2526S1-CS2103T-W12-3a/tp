package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.logic.commands.CommandResult;

/**
 * Unit tests for {@link StatsCommand}.
 * - Counts number of persons per tag across the entire address book.
 * - If no tags exist at all, returns "No tags found on any contact."
 * - Otherwise prints:
 *      Tag stats:
 *      • tag1: count
 *      • tag2: count
 *   sorted by descending count, then alphabetical by tag.
 */
public class StatsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_typicalAddressBook_success() {
        // In TypicalPersons:
        // ALICE -> friends
        // BENSON -> owesMoney, friends
        // DANIEL -> friends
        // Others have no tags
        String expectedMessage = String.join("\n",
                "Tag stats:",
                "• friends: 3",
                "• owesMoney: 1");

        StatsCommand command = new StatsCommand();
        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        // Model should not be mutated by read-only stats command
        assertEquals(model.getAddressBook(), expectedModel.getAddressBook());
        assertEquals(model.getFilteredPersonList(), expectedModel.getFilteredPersonList());
    }

    @Test
    public void execute_noTagsAnywhere_showsNoTagsMessage() {
        // Build an address book where no person has tags
        AddressBook ab = new AddressBook();

        Person p1 = new PersonBuilder().withName("No Tags One").withTags().build();
        Person p2 = new PersonBuilder().withName("No Tags Two").withTags().build();
        ab.addPerson(p1);
        ab.addPerson(p2);

        Model emptyTagsModel = new ModelManager(ab, new UserPrefs());

        StatsCommand command = new StatsCommand();
        CommandResult result = command.execute(emptyTagsModel);

        assertEquals("No tags found on any contact.", result.getFeedbackToUser());
    }

    @Test
    public void execute_countsAndOrdering_respectsSortRules() {
        // Start from typical book (friends:3, owesMoney:1)
        // Add one more person with tag "friends" and another with tag "alpha"
        // Expected counts: friends:4, alpha:1, owesMoney:1
        // Order: friends (4) first, then alpha (1) vs owesMoney (1) -> alphabetical: alpha before owesMoney

        Person extraFriend = new PersonBuilder().withName("Extra Friend").withTags("friends").build();
        Person alphaTag = new PersonBuilder().withName("Alpha Guy").withTags("alpha").build();
        model.addPerson(extraFriend);
        model.addPerson(alphaTag);

        String expectedMessage = String.join("\n",
                "Tag stats:",
                "• friends: 4",
                "• alpha: 1",
                "• owesMoney: 1");

        StatsCommand command = new StatsCommand();
        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
    }
}