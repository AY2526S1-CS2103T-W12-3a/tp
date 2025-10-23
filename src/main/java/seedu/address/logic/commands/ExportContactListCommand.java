package seedu.address.logic.commands;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;
import seedu.address.model.person.Person;



/**
 * Exports the current contact list (filtered or full) into a CSV file.
 * <p>
 * This command allows users to specify a custom filename or,
 * if omitted, automatically generates a timestamped file name.
 * The exported file is saved inside the {@code data/exports/} directory,
 * which is created automatically if it does not exist.
 * <p>
 * In addition, the command handles duplicate filenames by appending
 * numeric suffixes, escapes CSV-sensitive fields, and produces a
 * summary report showing the export path, number of contacts written,
 * and total execution time.
 */
public class ExportContactListCommand extends Command {

    /** Command word to trigger export functionality. */
    public static final String COMMAND_WORD = "export";

    /** Usage message for the export command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all (or filtered) contacts to a CSV file.\n"
            + "Parameters: [FILENAME]\n"
            + "Example: export my_contacts.csv";

    /** Message template for successful exports. */
    public static final String MESSAGE_SUCCESS = "Contacts successfully exported to:\n%s\n"
            + "Summary: %d contacts written in %.2f seconds.";

    /** Message template for failed exports. */
    public static final String MESSAGE_FAILURE = "Export failed due to: %s";

    /** Default export folder within the project. */
    private static final String EXPORT_DIRECTORY = "data/exports/";

    /** Default filename prefix used when no name is provided. */
    private static final String DEFAULT_PREFIX = "contacts_";

    /** Default file extension for all exported files. */
    private static final String DEFAULT_EXTENSION = ".csv";

    private static final Logger logger = LogsCenter.getLogger(ExportContactListCommand.class);

    /** Optional filename argument provided by the user. */
    private final String fileNameArgument;

    /**
     * Constructs an {@code ExportContactListCommand} with an optional filename argument.
     *
     * @param fileNameArgument User-provided filename (may be {@code null} or empty).
     */
    public ExportContactListCommand(String fileNameArgument) {
        this.fileNameArgument = fileNameArgument;
    }

    /**
     * Executes the export operation.
     * <p>
     * Writes all persons from the current filtered list to a CSV file.
     * If the user does not specify a filename, a timestamped name is generated.
     * Duplicate filenames are handled safely by appending numeric suffixes.
     *
     * @param model The model containing the current address book state.
     * @return A {@code CommandResult} summarizing the outcome of the export.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        logger.info("Export: start");
        long startTime = System.currentTimeMillis();

        List<Person> persons = model.getFilteredPersonList();
        if (persons.isEmpty()) {
            logger.info("Export: aborted (filtered list empty)");
            return new CommandResult("No contacts to export. The current list is empty.");
        }

        try {
            File exportDir = new File(EXPORT_DIRECTORY);
            if (!exportDir.exists()) {
                boolean created = exportDir.mkdirs();
                if (!created) {
                    throw new IOException("Could not create export directory: " + EXPORT_DIRECTORY);
                }
            }

            String chosenName = determineFilename();
            File exportFile = getUniqueFile(exportDir, chosenName);
            logger.info(() -> "Export: writing " + persons.size()
                    + " contacts to " + exportFile.getAbsolutePath());
            int count = writeContactsToCsv(exportFile, persons);

            double duration = (System.currentTimeMillis() - startTime) / 1000.0;
            return new CommandResult(String.format(MESSAGE_SUCCESS,
                    exportFile.getAbsolutePath(), count, duration));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Export: failed", e);
            return new CommandResult(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    /**
     * Determines the final output filename to be used.
     * <p>
     * If a filename argument is provided, it is used directly (appending
     * {@code .csv} if missing). Otherwise, a new timestamped filename is generated.
     *
     * @return A valid filename string ending with {@code .csv}.
     */
    private String determineFilename() {
        String filename;
        if (fileNameArgument != null && !fileNameArgument.trim().isEmpty()) {
            filename = fileNameArgument.trim();
        } else {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            filename = DEFAULT_PREFIX + timestamp + DEFAULT_EXTENSION;
        }

        if (!filename.endsWith(".csv")) {
            filename += DEFAULT_EXTENSION;
        }

        return filename;
    }

    /**
     * Ensures that the final export file does not overwrite an existing file.
     * <p>
     * If a file with the same name already exists, a numeric suffix is appended
     * to generate a unique filename.
     *
     * @param dir The directory in which to save the file.
     * @param filename The base filename to start with.
     * @return A {@code File} object guaranteed to have a unique path.
     */
    private File getUniqueFile(File dir, String filename) {
        File file = new File(dir, filename);
        int counter = 1;
        while (file.exists()) {
            String baseName = filename.replace(".csv", "");
            file = new File(dir, baseName + "_" + counter + ".csv");
            counter++;
        }
        return file;
    }

    /**
     * Writes all contacts from the provided list into the specified CSV file.
     * <p>
     * The first line contains the column headers. Each subsequent line represents
     * one {@code Person} entry.
     *
     * @param file The output file to write to.
     * @param persons The list of persons to export.
     * @return The total number of contacts successfully written.
     * @throws IOException If any file I/O errors occur during writing.
     */
    private int writeContactsToCsv(File file, List<Person> persons) throws IOException {
        int written = 0;
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Name,Phone,Email,Address,Tags\n");
            for (Person p : persons) {
                writer.write(formatCsvRow(p));
                written++;
            }
        }
        return written;
    }

    /**
     * Converts a {@code Person} object into a single valid CSV row.
     * <p>
     * Each field is escaped to preserve commas, quotes, and newline characters.
     *
     * @param p The person to be converted.
     * @return A formatted CSV string representing the person.
     */
    private String formatCsvRow(Person p) {
        String name = escapeCsv(p.getName().toString());
        String phone = escapeCsv(p.getPhone().toString());
        String email = escapeCsv(p.getEmail().toString());
        String address = escapeCsv(p.getAddress().toString());
        String tags = escapeCsv(p.getTags().toString().replaceAll("[\\[\\]]", ""));
        return String.format("%s,%s,%s,%s,%s\n", name, phone, email, address, tags);
    }

    /**
     * Escapes special characters to ensure CSV compatibility.
     * <p>
     * - Double quotes are doubled.<br>
     * - Fields containing commas or newlines are wrapped in quotes.
     *
     * @param value The raw field value to escape.
     * @return A CSV-safe string.
     */
    private String escapeCsv(String value) {
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n")) {
            escaped = "\"" + escaped + "\"";
        }
        return escaped;
    }
}
