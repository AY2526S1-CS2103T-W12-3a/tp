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
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.export.ColumnSpec;
import seedu.address.logic.export.ExportSchemas;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonReadOnly;
import seedu.address.model.util.CsvUtil;



/**
 * Exports the current contact list (filtered or full) into a CSV file.
 * <p>
 * Supports schema profiles via {@code --profile standard|full}.
 * - STANDARD: Name, Phone, Email, Address, Tags
 * - FULL: STANDARD + Role, Cadence, InteractionsCount
 * <p>
 * Files are written to {@code data/exports/}. If no filename is given,
 * a timestamped filename is used. Existing filenames are not overwritten.
 */
public class ExportContactListCommand extends Command {

    /** Command word to trigger export functionality. */
    public static final String COMMAND_WORD = "export";

    /** Usage message for the export command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports contacts to a CSV file.\n"
            + "Parameters: [FILENAME] [--profile standard|full]\n"
            + "Examples:\n"
            + "  export\n"
            + "  export my_contacts.csv\n"
            + "  export --profile full team.csv";

    /** Message template for successful exports. */
    public static final String MESSAGE_SUCCESS = "Contacts successfully exported to:\n%s\n"
            + "Summary: %d contacts written in %.2f seconds (profile: %s).";

    /** Message template for failed exports. */
    public static final String MESSAGE_FAILURE = "Export failed due to: %s";

    /** Default export folder within the project. */
    private static final String EXPORT_DIRECTORY = "data/exports/";

    /** Default filename prefix used when no name is provided. */
    private static final String DEFAULT_PREFIX = "contacts_";

    /** Default file extension for all exported files. */
    private static final String DEFAULT_EXTENSION = ".csv";

    private static final Logger logger = LogsCenter.getLogger(ExportContactListCommand.class);

    /** CSV export schema profiles. */
    public enum Profile { STANDARD, FULL }

    /** Optional filename argument provided by the user. */
    private final String fileNameArgument;

    private final Profile profile;

    /**
     * Constructs an {@code ExportContactListCommand}.
     *
     * @param fileNameArgument User-provided filename (may be {@code null} or blank).
     * @param profile Schema selection (defaults to STANDARD if {@code null}).
     */
    public ExportContactListCommand(String fileNameArgument, Profile profile) {
        this.fileNameArgument = (fileNameArgument == null || fileNameArgument.isBlank())
                ? null : fileNameArgument.trim();
        this.profile = (profile == null) ? Profile.STANDARD : profile;
    }

    /**
     * Executes the export operation.
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
                    exportFile.getAbsolutePath(), count, duration, profile.name().toLowerCase()));

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
            String baseName = filename.endsWith(".csv")
                    ? filename.substring(0, filename.length() - 4)
                    : filename;
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
        final var schema = selectSchema(profile);
        int written = 0;

        try (FileWriter writer = new FileWriter(file)) {
            // header
            CsvUtil.writeHeader(
                    schema.stream().map(c -> c.header)
                            .collect(Collectors.toList()), writer
            );

            for (Person p : persons) {
                PersonReadOnly pr = p; // Person implements PersonReadOnly
                var cells = new java.util.ArrayList<String>(schema.size());
                for (ColumnSpec<PersonReadOnly> col : schema) {
                    String v;
                    try {
                        v = String.valueOf(col.extractor.apply(pr));
                    } catch (Exception e) {
                        v = "";
                    } // defensive: empty on extractor errors
                    cells.add(v);
                }
                CsvUtil.writeRow(cells, writer);
                written++;
            }
        }
        return written;
    }

    /** Returns the selected export schema for the given profile. */
    private List<ColumnSpec<PersonReadOnly>> selectSchema(ExportContactListCommand.Profile profile) {
        return (profile == ExportContactListCommand.Profile.FULL)
                ? ExportSchemas.full() : ExportSchemas.standard();
    }
}
