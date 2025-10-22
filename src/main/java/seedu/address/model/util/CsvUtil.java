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
 * Utility class for reading {@link Person} objects from a CSV file.
 * <p>
 * This class provides a single method, {@link #readPersonsFromCsv(Path)},
 * which parses a comma-separated file containing person information.
 * Each non-empty line should be in the following format:
 * <pre>
 * Name, Phone, Email, [Address], [Tags separated by ';']
 * </pre>
 * Fields in brackets are optional. Malformed lines are skipped gracefully.
 */
public class CsvUtil {

    private static final Logger logger = LogsCenter.getLogger(CsvUtil.class);

    private static final int MIN_FIELDS = 3; // Name, Phone, Email
    private static final int MAX_FIELDS = 5; // Includes optional Address and Tags

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

                if (line.isEmpty()) {
                    continue; // skip blank lines
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
}
