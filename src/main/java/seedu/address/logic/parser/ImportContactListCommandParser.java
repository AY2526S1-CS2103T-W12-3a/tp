package seedu.address.logic.parser;

import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ImportContactListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link ImportContactListCommand} object.
 */
public class ImportContactListCommandParser implements Parser<ImportContactListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@link ImportContactListCommand}
     * and returns an {@link ImportContactListCommand} object for execution.
     *
     * @param args the user input string following the command word.
     * @return a new ImportContactListCommand ready for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public ImportContactListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException("File path required.\n" + ImportContactListCommand.MESSAGE_USAGE);
        }

        Path path = Paths.get(trimmedArgs);
        return new ImportContactListCommand(path);
    }
}
