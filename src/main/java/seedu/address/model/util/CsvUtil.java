package seedu.address.model.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * CSV utilities for the address book domain.
 * <p>
 * <b>Export:</b> Provides {@link #writeHeader(List, java.io.Writer)},
 * {@link #writeRow(List, java.io.Writer)}, and {@link #escape(String)} to write
 * CSV safely. Fields containing commas, quotes, or newlines are escaped by
 * doubling quotes and wrapping the cell in quotes.
 * <p>
 * <b>Import:</b> Provides {@link #readPersonsFromCsv(Path)} which parses a
 * simplified CSV containing people in the order:
 * <pre>Name, Phone, Email, [Address], [Tags separated by ';']</pre>
 * Address and tags are optional. Malformed lines are skipped.
 * <p><strong>Note:</strong> The current importer is a simple splitter and does
 * not honor quoted fields. If a field contains commas or newlines, it must be
 * fixed in a future iteration with a proper CSV parser.
 * <p>
 * This class is stateless and thread-safe.
 */
public class CsvUtil {

    private static final Logger logger = LogsCenter.getLogger(CsvUtil.class);

    private static final int MIN_FIELDS = 3; // Name, Phone, Email
    private static final int MAX_FIELDS = 5; // Includes optional Address and Tags
    private static final char DELIM = ',';
    private static final String NEWLINE = "\n";

    /**
     * Reads a list of {@link Person} objects from a CSV file.
     *
     * @param filePath Path to the CSV file.
     * @return List of valid {@link Person} objects parsed from the file.
     * @throws IOException              If there is an error reading the file.
     * @throws IllegalArgumentException If the file content is malformed.
     */
    public static List<Person> readPersonsFromCsv(Path filePath) throws IOException {
        requireNonNull(filePath);
        assert Files.exists(filePath) : "CSV file path must exist before reading.";

        List<Person> persons = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.toLowerCase().startsWith("name,phone,email")) {
                    continue;
                }

                try {
                    Person person = parsePersonLine(line);
                    persons.add(person);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING,
                            String.format("Skipping malformed line %d: %s (%s)", lineNumber, line, e.getMessage()));
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read CSV file: " + filePath, e);
            throw e;
        }

        logger.info("Parsed " + persons.size() + " valid contacts from CSV: " + filePath);
        return persons;
    }

    /**
     * Parses a single CSV line into a {@link Person} object.
     *
     * @param line A CSV line containing fields for a person.
     * @return A {@link Person} instance created from the line.
     * @throws IllegalArgumentException If the line is malformed or missing fields.
     */
    private static Person parsePersonLine(String line) {
        String[] fields = line.split(",", -1); // include empty fields

        if (fields.length < MIN_FIELDS || fields.length > MAX_FIELDS) {
            throw new IllegalArgumentException("Invalid number of fields (" + fields.length + ")");
        }

        String nameStr = fields[0].trim();
        String phoneStr = fields[1].trim();
        String emailStr = fields[2].trim();

        if (nameStr.isEmpty() || phoneStr.isEmpty() || emailStr.isEmpty()) {
            throw new IllegalArgumentException("Missing required fields (name/phone/email).");
        }

        Name name = new Name(nameStr);
        Phone phone = new Phone(phoneStr);
        Email email = new Email(emailStr);

        Address address = (fields.length >= 4 && !fields[3].trim().isEmpty())
                ? new Address(fields[3].trim())
                : new Address("");

        Set<Tag> tags = new HashSet<>();
        if (fields.length == 5 && !fields[4].trim().isEmpty()) {
            String[] tagStrings = fields[4].split(";");
            for (String tagStr : tagStrings) {
                tags.add(new Tag(tagStr.trim()));
            }
        }

        return new Person(name, phone, email, address, tags);
    }

    /**
     * Escapes a string for CSV output.
     * <ul>
     *   <li>Doubles any double quotes ({@code " -> ""}).</li>
     *   <li>If the cell contains the delimiter, a quote, or a newline, wraps the
     *       entire cell in double quotes.</li>
     *   <li>Null values are rendered as empty strings.</li>
     * </ul>
     *
     * @param s the cell value, possibly {@code null}
     * @return an export-safe CSV cell
     */
    public static String escape(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.indexOf(DELIM) >= 0 || s.indexOf('"') >= 0
                || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0;
        String v = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }

    /**
     * Writes a header row to the writer using the current delimiter.
     * Calls {@link #writeRow(List, java.io.Writer)} under the hood.
     *
     * @param headers ordered list of column names (nulls rendered empty)
     * @param out the writer to append to
     * @throws IOException if writing fails
     */

    public static void writeHeader(List<String> headers, java.io.Writer out) throws IOException {
        writeRow(headers, out);
    }

    public static void writeRow(List<String> cells, java.io.Writer out) throws IOException {
        // Null-safe and escape each cell
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.size(); i++) {
            String cell = cells.get(i);
            sb.append(escape(cell == null ? "" : cell));
            if (i + 1 < cells.size()) sb.append(DELIM);
        }
        sb.append(NEWLINE);
        out.write(sb.toString());
    }
}
