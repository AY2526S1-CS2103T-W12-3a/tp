package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import seedu.address.model.interaction.Interaction;
import seedu.address.model.interaction.InteractionType;

public class JsonAdaptedInteractionTest {

    @Test
    public void toModelType_roundTrip_success() {
        Interaction src = new Interaction(InteractionType.CALL, "Quick sync",
                Instant.parse("2025-02-02T02:02:02Z"));
        JsonAdaptedInteraction json = new JsonAdaptedInteraction(src);
        Interaction back = json.toModelType();

        assertEquals(src.getType(), back.getType());
        assertEquals(src.getDetails(), back.getDetails());
        assertEquals(src.getTimestamp(), back.getTimestamp());
    }

    @Test
    public void toModelType_invalidType_throws() {
        JsonAdaptedInteraction bad = new JsonAdaptedInteraction("fax", "nope", "2025-01-01T00:00:00Z");
        assertThrows(IllegalArgumentException.class, bad::toModelType);
    }
}
