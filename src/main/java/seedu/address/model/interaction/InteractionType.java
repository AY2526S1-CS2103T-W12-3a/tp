package seedu.address.model.interaction;

/** Type of an interaction logged for a Person. */
public enum InteractionType {
    CALL, EMAIL, MEETING, NOTE;

    /** Case-insensitive parse with a friendly error. */
    public static InteractionType parse(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Interaction type cannot be null.");
        }
        switch (raw.trim().toLowerCase()) {
        case "call":
            return CALL;
        case "email":
            return EMAIL;
        case "meeting":
            return MEETING;
        case "note":
            return NOTE;
        default:
            throw new IllegalArgumentException(
                "Invalid interaction type. Use one of: call, email, meeting, note.");
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
