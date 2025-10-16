package seedu.address.model.interaction;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/** Immutable record of a single interaction. */
public final class Interaction {

    private static final int MAX_LEN = 500;
    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final InteractionType type;
    private final String details;
    private final Instant timestamp;

    /**
     * Creates an {@code Interaction}.
     * @param type interaction type (e.g., CALL, EMAIL)
     * @param details non-empty details text
     * @param timestamp time of the interaction (UTC instant)
     */
    public Interaction(InteractionType type, String details, Instant timestamp) {
        requireNonNull(type);
        requireNonNull(details);
        requireNonNull(timestamp);

        String trimmed = details.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Details cannot be empty.");
        }
        if (trimmed.length() > MAX_LEN) {
            throw new IllegalArgumentException("Details too long (max " + MAX_LEN + " chars).");
        }

        this.type = type;
        this.details = trimmed;
        this.timestamp = timestamp;
    }

    public InteractionType getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    /** Short pretty line suitable for PersonCard like: "email · 2025-10-15 17:05". */
    public String toDisplayString(ZoneId zone) {
        return type + " · " + DISPLAY_FMT.format(timestamp.atZone(zone));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interaction)) {
            return false;
        }
        Interaction that = (Interaction) o;
        return type == that.type
                && details.equals(that.details)
                && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, details, timestamp);
    }

    @Override
    public String toString() {
        return type + " | " + details + " @ " + timestamp;
    }
}
