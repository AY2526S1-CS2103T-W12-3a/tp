package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LogCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.interaction.InteractionType;

/**
 * Parses input arguments and creates a new {@link LogCommand}.
 */
public class LogCommandParser implements Parser<LogCommand> {
    @Override
    public LogCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INTERACTION_TYPE, PREFIX_DETAILS);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        if (!argMultimap.getValue(PREFIX_INTERACTION_TYPE).isPresent()
                || !argMultimap.getValue(PREFIX_DETAILS).isPresent()) {
            throw new ParseException(LogCommand.MESSAGE_USAGE);
        }

        String typeRaw = argMultimap.getValue(PREFIX_INTERACTION_TYPE).get();
        String details = argMultimap.getValue(PREFIX_DETAILS).get();

        InteractionType type;
        try {
            type = InteractionType.parse(typeRaw);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return new LogCommand(index, type, details);
    }
}
