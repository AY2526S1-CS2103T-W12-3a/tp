package seedu.address.model.interaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class InteractionTest {

    @Test
    public void constructor_validFields_success() {
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        Interaction i = new Interaction(InteractionType.EMAIL, "Sent deck", t);
        assertEquals(InteractionType.EMAIL, i.getType());
        assertEquals("Sent deck", i.getDetails());
        assertEquals(t, i.getTimestamp());
    }

    @Test
    public void constructor_emptyDetails_throws() {
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(InteractionType.CALL, "   ", t));
    }
}
