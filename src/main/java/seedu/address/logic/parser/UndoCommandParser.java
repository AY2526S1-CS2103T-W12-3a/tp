package seedu.address.logic.parser;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UndoCommand object
 */
public class UndoCommandParser implements Parser<UndoCommand> {
    @Override
    public UndoCommand parse(String args) throws ParseException {
        // no arguments expected
        return new UndoCommand();
    }
}
