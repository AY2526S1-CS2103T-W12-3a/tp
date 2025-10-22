package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the most recent change made to the address book.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful.";
    public static final String MESSAGE_FAILURE = "Nothing to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.undoState()) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            throw new CommandException(MESSAGE_FAILURE);
        }
    }
}
