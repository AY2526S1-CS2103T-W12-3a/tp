package seedu.address.logic.parser;

import seedu.address.logic.commands.ExportContactListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.ExportContactListCommand.Profile;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Objects.requireNonNull;

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
    private static final Pattern PROFILE_PATTERN =
            Pattern.compile("(?i)(?:^|\\s)--profile\\s+(\\S+)(?=\\s|$)");

    private static final Pattern PROFILE_BARE_PATTERN =
            Pattern.compile("(?i)(?:^|\\s)--profile(?:\\s|$)");
    /**
     * Parses the given {@code String} of arguments and returns an {@code ExportContactListCommand}.
     *
     * @param args The user input following the command word.
     * @return A new {@code ExportContactListCommand} containing the filename argument (if any).
     * @throws ParseException If any parsing errors occur (not expected for this simple command).
     */
    @Override
    public ExportContactListCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        Profile profile = Profile.STANDARD;
        String filePart = trimmed;
        boolean hasBareProfile = PROFILE_BARE_PATTERN.matcher(trimmed).find()
                && !PROFILE_PATTERN.matcher(trimmed).find();
        if (hasBareProfile) {
            throw new ParseException("Missing value for --profile. Use: --profile standard | --profile full");
        }
        Matcher m = PROFILE_PATTERN.matcher(trimmed);
        if (m.find()) {
            String raw = m.group(1);
            String val = raw.toLowerCase(Locale.ROOT);

            if (m.find()) {
                throw new ParseException(
                        "Duplicate --profile flags. Use exactly one of: --profile standard | --profile full");
            }

            switch (val) {
                case "standard":
                    profile = Profile.STANDARD;
                    break;
                case "full":
                    profile = Profile.FULL;
                    break;
                default:
                    throw new ParseException(
                            "Unknown profile '" + raw + "'. Allowed: standard, full. " +
                                    "Example: export team.csv --profile full");
            }
            filePart = PROFILE_PATTERN.matcher(filePart).replaceAll(" ").trim();
            filePart = PROFILE_BARE_PATTERN.matcher(filePart).replaceAll(" ").trim();
        }
        String cleaned = (filePart == null) ? "" : filePart.trim();
        if (cleaned.isEmpty()) {
            return new ExportContactListCommand(null, profile);
        }
        if (cleaned.startsWith("--")) {
            throw new ParseException(
                    "Unexpected flag '" + cleaned + "'. Filename should not start with '--'. "
                            + "Example: export team.csv --profile full"
            );
        }
        return new ExportContactListCommand(cleaned, profile);
    }
}
