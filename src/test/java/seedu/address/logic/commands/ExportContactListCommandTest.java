package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.util.SampleDataUtil;

/**
 * Integration tests for {@link ExportContactListCommand}.
 * <p>
 * These tests verify correct CSV file creation, naming behavior,
 * directory handling, and feedback messages for both default and
 * custom export scenarios.
 */
public class ExportContactListCommandTest {

    private static final String EXPORT_DIR = "data/exports/";
    private Model model;

    /**
     * Sets up a test model with sample data before each test case.
     */
    @BeforeEach
    public void setUp() {
        AddressBook sampleAddressBook = new AddressBook();
        for (Person p : SampleDataUtil.getSamplePersons()) {
            sampleAddressBook.addPerson(p);
        }
        model = new ModelManager(sampleAddressBook, new UserPrefs());
    }

    /**
     * Cleans up all files inside the export directory after each test
     * to ensure isolation between test cases.
     */
    @AfterEach
    public void tearDown() throws Exception {
        File dir = new File(EXPORT_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    Files.deleteIfExists(f.toPath());
                }
            }
        }
    }

    /**
     * Tests exporting without specifying a filename.
     * <p>
     * Expected behavior:
     * - A timestamped file is created inside data/exports/
     * - The command result message confirms successful export
     */
    @Test
    public void execute_exportDefaultFile_success() {
        ExportContactListCommand command = new ExportContactListCommand(null,
                ExportContactListCommand.Profile.STANDARD);
        CommandResult result = command.execute(model);

        // Message check
        assertTrue(
                result.getFeedbackToUser().contains("Contacts successfully exported")
                        && result.getFeedbackToUser().toLowerCase().contains("(profile: standard)"),
                "Feedback should indicate successful export and show profile: standard."
        );

        // Directory check
        File dir = new File(EXPORT_DIR);
        assertTrue(dir.exists(), "Export directory should be created.");

        // At least one file exists
        File[] files = dir.listFiles();
        assertTrue(files != null && files.length > 0, "At least one CSV file should be created.");
    }

    /**
     * Tests exporting with a custom filename provided.
     * <p>
     * Expected behavior:
     * - The specified file is created inside data/exports/
     * - The command result confirms the correct path
     */
    @Test
    public void execute_exportWithCustomFilename_success() {
        String filename = "test_contacts.csv";
        ExportContactListCommand command = new ExportContactListCommand(filename,
                ExportContactListCommand.Profile.STANDARD);
        CommandResult result = command.execute(model);

        // Confirm feedback message
        assertTrue(
                result.getFeedbackToUser().contains("Contacts successfully exported")
                        && result.getFeedbackToUser().toLowerCase().contains("(profile: standard)"),
                "Feedback should confirm export success and show profile: standard."
        );

        // Confirm file created
        File exported = new File(EXPORT_DIR + filename);
        assertTrue(exported.exists(), "Expected file should exist in export directory.");
    }

    /**
     * Tests export command behavior when run multiple times
     * with the same filename argument.
     * <p>
     * Expected behavior:
     * - The first export creates the base file (test_contacts.csv)
     * - The second export auto-renames to avoid overwrite (test_contacts_1.csv)
     */
    @Test
    public void execute_duplicateFilename_createsIncrementedFile() {
        String filename = "duplicate_test.csv";

        // First export
        ExportContactListCommand firstCommand = new ExportContactListCommand(filename,
                ExportContactListCommand.Profile.STANDARD);
        CommandResult firstResult = firstCommand.execute(model);
        assertTrue(firstResult.getFeedbackToUser().contains("Contacts successfully exported"),
                "First export should succeed.");

        // Second export
        ExportContactListCommand secondCommand = new ExportContactListCommand(filename,
                ExportContactListCommand.Profile.STANDARD);
        CommandResult secondResult = secondCommand.execute(model);
        assertTrue(secondResult.getFeedbackToUser().contains("Contacts successfully exported"),
                "Second export should also succeed.");

        // Check both files exist
        File file1 = new File(EXPORT_DIR + filename);
        File file2 = new File(EXPORT_DIR + "duplicate_test_1.csv");
        assertTrue(file1.exists(), "Original file should exist.");
        assertTrue(file2.exists(), "Duplicate should be renamed and created.");
    }

    /**
     * Tests export behavior when the model contains no contacts.
     * <p>
     * Expected behavior:
     * - No file is written
     * - The feedback message indicates no contacts to export
     */
    @Test
    public void execute_emptyModel_noContactsMessage() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        ExportContactListCommand command = new ExportContactListCommand("empty_test.csv",
                ExportContactListCommand.Profile.STANDARD);
        CommandResult result = command.execute(emptyModel);

        assertTrue(result.getFeedbackToUser().contains("No contacts to export"),
                "Feedback should indicate empty list handling.");
    }

    @Test
    public void execute_profileFull_writesFullSchema() {
        ExportContactListCommand command = new ExportContactListCommand("full_profile.csv",
                ExportContactListCommand.Profile.FULL); // CHANGED: USE PROFILE.FULL
        CommandResult result = command.execute(model);
        assertTrue(
                result.getFeedbackToUser().contains("Contacts successfully exported")
                        && result.getFeedbackToUser().toLowerCase().contains("(profile: full)"),
                "Feedback should show profile: full."
        );
        File exported = new File(EXPORT_DIR + "full_profile.csv");
        assertTrue(exported.exists(), "Full profile file should be created.");
    }
}
