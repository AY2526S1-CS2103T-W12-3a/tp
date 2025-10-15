package seedu.address.logic.parser;

import seedu.address.logic.commands.ExportContactListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ExportContactListCommand} object.
 * <p>
 * This parser supports an optional filename argument. If the user provides a filename,
 * the command will export the contacts to that file; otherwise, it will automatically
 * generate a timestamped filename.
 * <p>
 * Example usages:
 * <ul>
 *   <li>{@code export}</li>
 *   <li>{@code export my_contacts.csv}</li>
 * </ul>
 */
public class ExportContactListCommandParser implements Parser<ExportContactListCommand> {

    /**
     * Parses the given {@code String} of arguments and returns an {@code ExportContactListCommand}.
     *
     * @param args The user input following the command word.
     * @return A new {@code ExportContactListCommand} containing the filename argument (if any).
     * @throws ParseException If any parsing errors occur (not expected for this simple command).
     */
    @Override
    public ExportContactListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        return new ExportContactListCommand(trimmedArgs.isEmpty() ? null : trimmedArgs);
    }
}
