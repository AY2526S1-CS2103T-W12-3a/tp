package seedu.address.model.interaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.ZoneId;

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
    public void constructor_trimsDetails_success() {
        Instant t = Instant.parse("2025-03-03T03:03:03Z");
        Interaction i = new Interaction(InteractionType.CALL, "   intro call   ", t);
        assertEquals("intro call", i.getDetails());
    }

    @Test
    public void constructor_emptyDetails_throws() {
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(InteractionType.CALL, "   ", t));
    }

    @Test
    public void constructor_tooLongDetails_throws() {
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        String over = "a".repeat(501); // max is 500
        assertThrows(IllegalArgumentException.class, () -> new Interaction(InteractionType.NOTE, over, t));
    }

    @Test
    public void equalsAndHashCode_sameFields_equal() {
        Instant t = Instant.parse("2024-12-12T12:00:00Z");
        Interaction a = new Interaction(InteractionType.CALL, "Intro call", t);
        Interaction b = new Interaction(InteractionType.CALL, "Intro call", t);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentType_notEqual() {
        Instant t = Instant.parse("2024-12-12T12:00:00Z");
        Interaction a = new Interaction(InteractionType.CALL, "Intro call", t);
        Interaction b = new Interaction(InteractionType.EMAIL, "Intro call", t);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentDetails_notEqual() {
        Instant t = Instant.parse("2024-12-12T12:00:00Z");
        Interaction a = new Interaction(InteractionType.MEETING, "Pitch v1", t);
        Interaction b = new Interaction(InteractionType.MEETING, "Pitch v2", t);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentTimestamp_notEqual() {
        Interaction a = new Interaction(InteractionType.NOTE, "Left a note",
            Instant.parse("2024-12-12T12:00:00Z"));
        Interaction b = new Interaction(InteractionType.NOTE, "Left a note",
            Instant.parse("2024-12-12T12:00:01Z"));
        assertNotEquals(a, b);
    }

    @Test
    public void toDisplayString_respectsZoneAndFormat() {
        // 2025-01-01 10:00Z = 2025-01-01 18:00 in Singapore (UTC+8)
        Interaction i = new Interaction(
            InteractionType.EMAIL, "Sent deck",
            Instant.parse("2025-01-01T10:00:00Z"));

        String sgt = i.toDisplayString(ZoneId.of("Asia/Singapore"));
        assertEquals("email · 2025-01-01 18:00", sgt);

        String utc = i.toDisplayString(ZoneId.of("UTC"));
        assertEquals("email · 2025-01-01 10:00", utc);
    }

    @Test
    public void toString_containsTypeDetailsAndInstant() {
        Interaction i = new Interaction(
            InteractionType.MEETING, "First pitch",
            Instant.parse("2025-02-02T08:30:00Z"));

        String s = i.toString();
        // Format: "meeting | First pitch @ 2025-02-02T08:30:00Z"
        assertTrue(s.startsWith("meeting | First pitch @ "));
        assertTrue(s.endsWith("Z")); // Instant string ends with 'Z'
        assertTrue(s.contains("2025-02-02T08:30:00Z"));
    }

}
