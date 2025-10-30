package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Integration tests for {@link ImportContactListCommand}.
 * <p>
 * These tests verify the following:
 * <ul>
 *   <li>Successful import from a valid CSV file</li>
 *   <li>Handling of missing file errors</li>
 *   <li>Detection of invalid CSV format</li>
 *   <li>Correct behavior with duplicate entries</li>
 *   <li>Logging and exception handling correctness</li>
 * </ul>
 */
public class ImportContactListCommandTest {

    private static final Path TEST_DIR = Path.of("data", "testdata");
    private static final Path VALID_FILE = TEST_DIR.resolve("valid_contacts.csv");
    private static final Path INVALID_FILE = TEST_DIR.resolve("invalid_contacts.csv");
    private static final Path DUPLICATE_FILE = TEST_DIR.resolve("duplicate_contacts.csv");
    private static final Path MISSING_FILE = TEST_DIR.resolve("missing_file.csv");

    private Model model;

    /**
     * Creates temporary CSV files for testing.
     */
    @BeforeEach
    public void setUp() throws IOException {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        Files.createDirectories(TEST_DIR);

        String header = String.join(",",
                "name", "phone", "email", "address", "tags", "role", "cadence", "interactions");

        List<String> validLines = List.of(
                header,
                "Alice Tan,91234567,alice@example.com,123 Orchard Road,friend,Customer,,",
                "Bob Lim,98765432,bob@example.com,456 Clementi Ave,school,Customer,,"
        );
        Files.write(VALID_FILE, validLines);

        List<String> invalidLines = List.of(
                header,
                "Alice Tan,,alice@example.com,123 Orchard Road,friend,Customer,,",
                "Bob Lim,98765432,,456 Clementi Ave,school,Customer,,"
        );
        Files.write(INVALID_FILE, invalidLines);
        List<String> duplicateLines = List.of(
                header,
                "Charlie Wong,99998888,charlie@ntu.edu.sg,Jurong West,,Lead,,",
                "Charlie Wong,99998888,charlie@ntu.edu.sg,Jurong West,,Lead,,"
        );
        Files.write(DUPLICATE_FILE, duplicateLines);
    }


    /**
     * Deletes temporary files after each test to ensure isolation.
     */
    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(VALID_FILE);
        Files.deleteIfExists(INVALID_FILE);
        Files.deleteIfExists(DUPLICATE_FILE);
        Files.deleteIfExists(MISSING_FILE);
    }

    /**
     * Verifies that a valid CSV imports correctly and adds new contacts to the model.
     */
    @Test
    public void execute_validFile_success() throws Exception {
        ImportContactListCommand command = new ImportContactListCommand(VALID_FILE);
        CommandResult result = command.execute(model);

        assertTrue(model.getAddressBook().getPersonList().size() == 2);
        assertTrue(model.hasPerson(new Person(
                new Name("Alice Tan"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123 Orchard Road"),
                Set.of(new Tag("friend"))
        )));

        assertEquals(
                String.format(ImportContactListCommand.MESSAGE_SUCCESS, 2, VALID_FILE),
                result.getFeedbackToUser()
        );
    }

    /**
     * Verifies that an error is thrown if the specified file does not exist.
     */
    @Test
    public void execute_missingFile_throwsCommandException() {
        ImportContactListCommand command = new ImportContactListCommand(MISSING_FILE);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    /**
     * Verifies that invalid file content triggers a CommandException.
     */
    @Test
    public void execute_invalidFile_throwsCommandException() {
        ImportContactListCommand command = new ImportContactListCommand(INVALID_FILE);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    /**
     * Verifies that duplicate contacts in the CSV are ignored safely.
     */
    @Test
    public void execute_duplicateContacts_skippedSuccessfully() throws Exception {
        ImportContactListCommand command = new ImportContactListCommand(DUPLICATE_FILE);
        CommandResult result = command.execute(model);

        assertEquals(1, model.getAddressBook().getPersonList().size());
        assertTrue(model.hasPerson(new Person(
                new Name("Charlie Wong"),
                new Phone("99998888"),
                new Email("charlie@ntu.edu.sg"),
                new Address("Jurong West"),
                Set.of()
        )));

        assertEquals(
                String.format(ImportContactListCommand.MESSAGE_SUCCESS, 1, DUPLICATE_FILE),
                result.getFeedbackToUser()
        );
    }

    /**
     * Verifies that an empty CSV file triggers a CommandException
     * because there are no valid contacts to import.
     */
    @Test
    public void execute_emptyFile_throwsCommandException() throws Exception {
        Path emptyFile = TEST_DIR.resolve("empty_contacts.csv");
        Files.write(emptyFile, List.of("name,phone,email,address,tags,role,cadence,interactions"));
        ImportContactListCommand command = new ImportContactListCommand(emptyFile);
        assertThrows(CommandException.class, () -> command.execute(model));
        Files.deleteIfExists(emptyFile);
    }

    @Test
    public void equals() {
        Path p1 = Path.of("a.csv");
        Path p2 = Path.of("b.csv");
        ImportContactListCommand c1 = new ImportContactListCommand(p1);
        ImportContactListCommand c2 = new ImportContactListCommand(p1);
        ImportContactListCommand c3 = new ImportContactListCommand(p2);
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertTrue(!c1.equals(c3));
        assertTrue(!c1.equals(null));
        assertTrue(!c1.equals("x"));
    }

    @Test
    public void execute_tabDelimited_success() throws Exception {
        Path f = TEST_DIR.resolve("tab_contacts.csv");
        List<String> lines = List.of(
                "name\tphone\temail\taddress\ttags\trole\tcadence\tinteractions",
                "Lee Ada\t81234567\tada@example.com\t123 Tech Park\tvip\tInvestor\t\t"
        );
        Files.write(f, lines);
        CommandResult r = new ImportContactListCommand(f).execute(model);
        assertEquals(1, model.getAddressBook().getPersonList().size());
        Files.deleteIfExists(f);
    }
}
