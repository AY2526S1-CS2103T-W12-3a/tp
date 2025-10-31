package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.util.CsvUtil;

/**
 * Imports a list of contacts from a CSV file into the AddressBook.
 * <p>
 * The CSV must have a header row (case-insensitive) containing at least:
 * Name, Role, Address, Phone, Email.
 * Optional columns: Tags, Cadence, Interactions.
 * Unknown columns are ignored.
 * <ul>
 *   <li>Name</li>
 *   <li>Role</li>
 *   <li>Address</li>
 *   <li>Phone number</li>
 *   <li>Email address</li>
 *   <li>(Optional) Tags</li>
 *   <li>(Optional) Cadence (days)</li>
 *   <li>(Optional) Interactions (count)</li>
 * </ul>
 * Duplicate contacts are ignored; malformed rows are skipped with warnings.
 */
public class ImportContactListCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_NO_VALID_CONTACTS = "CSV contained no valid contacts: %s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports contacts from a CSV file.\n"
            + "Parameters: FILE_PATH\n"
            + "Notes: CSV must include a header with at least Name, Role, Address, Phone, Email.\n"
            + "Example: " + COMMAND_WORD + " data/exports/contacts_backup.csv";

    public static final String MESSAGE_SUCCESS = "Successfully imported %d contacts from %s";
    public static final String MESSAGE_FILE_NOT_FOUND = "File not found: %s";
    public static final String MESSAGE_INVALID_FILE = "Invalid CSV format in file: %s";
    public static final String MESSAGE_READ_ERROR = "An error occurred while reading the file: %s";

    private static final Logger logger = LogsCenter.getLogger(ImportContactListCommand.class);

    private final Path filePath;

    /**
     * Constructs an {@code ImportContactListCommand} that imports contacts from the given file path.
     *
     * @param filePath Path to the CSV file containing contacts.
     */
    public ImportContactListCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    /**
     * Executes the import command by reading contacts from a CSV file and adding them to the model.
     *
     * @param model The {@link Model} which contains the address book.
     * @return A {@link CommandResult} indicating success and number of contacts imported.
     * @throws CommandException If the file is missing, unreadable, or has invalid content.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert model != null : "Model reference must not be null in ImportContactListCommand";

        logger.info("Starting import process for file: " + filePath);

        verifyFileExists(filePath);
        List<Person> persons = readPersonsFromCsv(filePath);

        if (persons.isEmpty()) {
            logger.warning("CSV contained no valid contacts: " + filePath);
            throw new CommandException(String.format(MESSAGE_NO_VALID_CONTACTS, filePath));
        }

        int importedCount = importPersonsIntoModel(model, persons);
        logger.info("Import completed successfully with " + importedCount + " new contacts.");

        return new CommandResult(String.format(MESSAGE_SUCCESS, importedCount, filePath));
    }

    /**
     * Verifies that the file exists before attempting to read it.
     *
     * @param path The path to check.
     * @throws CommandException If the file does not exist.
     */
    private void verifyFileExists(Path path) throws CommandException {
        if (!Files.exists(path)) {
            logger.warning("Import failed: file not found at " + path);
            throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, path));
        }
    }

    /**
     * Reads contacts from the given CSV file using {@link CsvUtil}.
     *
     * @param path Path to the CSV file.
     * @return A list of {@link Person} objects parsed from the file.
     * @throws CommandException If the file content is invalid or cannot be read.
     */
    private List<Person> readPersonsFromCsv(Path path) throws CommandException {
        try {
            List<Person> persons = CsvUtil.readPersonsFromCsv(path);
            assert persons != null : "CSV utility must return a non-null list of persons";
            return persons;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO error during import from " + path, e);
            throw new CommandException(String.format(MESSAGE_READ_ERROR, path));
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid CSV format in " + path + ": " + e.getMessage());
            throw new CommandException(String.format(MESSAGE_INVALID_FILE, path));
        }
    }

    /**
     * Adds valid, non-duplicate contacts to the {@link Model}.
     *
     * @param model   The {@link Model} instance managing persons.
     * @param persons The list of persons to import.
     * @return Number of contacts successfully imported.
     */
    private int importPersonsIntoModel(Model model, List<Person> persons) {
        assert persons != null : "Persons list must not be null before import";
        int count = 0;

        for (Person person : persons) {
            if (person == null) {
                logger.warning("Skipped null person entry during import.");
                continue;
            }
            if (model.hasPerson(person)) {
                logger.info("Duplicate contact skipped: " + person.getName());
                continue;
            }
            model.addPerson(person);
            count++;
        }
        return count;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ImportContactListCommand
                && filePath.equals(((ImportContactListCommand) other).filePath));
    }
}
