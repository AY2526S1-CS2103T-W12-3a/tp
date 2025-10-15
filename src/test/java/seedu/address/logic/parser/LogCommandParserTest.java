package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LogCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.interaction.InteractionType;

public class LogCommandParserTest {

    private LogCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new LogCommandParser();
    }

    @Test
    public void parse_validArgs_success() throws Exception {
        String input = "1 i/email d/Sent deck";
        LogCommand cmd = parser.parse(input);

        LogCommand expected = new LogCommand(Index.fromOneBased(1),
                InteractionType.EMAIL, "Sent deck");
        assertEquals(expected, cmd);
    }

    @Test
    public void parse_invalidType_throws() {
        String input = "2 i/fax d/nope";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}
