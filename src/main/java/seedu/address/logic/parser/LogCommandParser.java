package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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
            ArgumentTokenizer.tokenize(args, PREFIX_INTERACTION_TYPE, PREFIX_DETAILS, PREFIX_DELETE);

        String preamble = argMultimap.getPreamble().trim();

        // Show standard "invalid format" usage for bare `log` / missing index
        if (preamble.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                LogCommand.MESSAGE_USAGE));
        }

        // Split preamble into the index token and any stray text (e.g., "1 a/")
        String[] parts = preamble.split("\\s+", 2);
        String indexToken = parts[0];
        String stray = parts.length == 2 ? parts[1].trim() : "";

        // If there's anything after the index in the preamble, it's an invalid/unknown parameter.
        if (!stray.isEmpty()) {
            // If it looks like a prefix (e.g., a/), highlight that specifically.
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\b[\\p{Alpha}]+/").matcher(stray);
            String what = m.find() ? m.group() : stray;
            throw new ParseException("Invalid parameter: " + what
                + ". Allowed: i/<type>, d/<details>, del/.");
        }

        // Validate that the index token is a positive integer.
        boolean unsignedDigits = indexToken.chars().allMatch(Character::isDigit);
        boolean signedNegative = indexToken.matches("-\\d+");
        if (!(unsignedDigits || signedNegative)) {
            throw new ParseException(ParserUtil.MESSAGE_INDEX_NOT_NUMBER);
        }
        if ("0".equals(indexToken) || signedNegative) {
            throw new ParseException(ParserUtil.MESSAGE_INDEX_NOT_POSITIVE);
        }

        Index index = ParserUtil.parseIndex(indexToken);

        // Handle delete-last form: `log INDEX del/` (and nothing else).
        if (argMultimap.getValue(PREFIX_DELETE).isPresent()) {
            if (argMultimap.getValue(PREFIX_INTERACTION_TYPE).isPresent()
                || argMultimap.getValue(PREFIX_DETAILS).isPresent()) {
                throw new ParseException("To delete the last interaction, use: log INDEX del/");
            }
            return new LogCommand(index, true);
        }

        // Require both type and details.
        if (!argMultimap.getValue(PREFIX_INTERACTION_TYPE).isPresent()
            || !argMultimap.getValue(PREFIX_DETAILS).isPresent()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                LogCommand.MESSAGE_USAGE));
        }

        String typeRaw = argMultimap.getValue(PREFIX_INTERACTION_TYPE).get();
        String details = argMultimap.getValue(PREFIX_DETAILS).get();

        if (details.trim().isEmpty()) {
            throw new ParseException("Details cannot be empty. Please provide a message after d/.");
        }

        if (details.length() > 500) {
            throw new ParseException("Details too long (max 500 characters).");
        }

        InteractionType type;
        try {
            type = InteractionType.parse(typeRaw);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return new LogCommand(index, type, details);
    }
}
