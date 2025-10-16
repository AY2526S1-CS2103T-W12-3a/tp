package seedu.address.storage;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.interaction.Interaction;
import seedu.address.model.interaction.InteractionType;

/** Jackson-friendly version of {@link Interaction}. */
public class JsonAdaptedInteraction {

    private final String type;
    private final String details;
    private final String timestamp; // ISO-8601

    /**
     * Constructs a {@code JsonAdaptedInteraction} from JSON properties.
     */
    @JsonCreator
    public JsonAdaptedInteraction(@JsonProperty("type") String type,
                                  @JsonProperty("details") String details,
                                  @JsonProperty("timestamp") String timestamp) {
        this.type = type;
        this.details = details;
        this.timestamp = timestamp;
    }

    /**
     * Converts a given {@code Interaction} into this class for Jackson use.
     */
    public JsonAdaptedInteraction(Interaction src) {
        this.type = src.getType().toString();
        this.details = src.getDetails();
        this.timestamp = src.getTimestamp().toString();
    }

    /**
     * Converts this Jackson-friendly object back into the model's {@code Interaction}.
     */
    public Interaction toModelType() {
        return new Interaction(
                InteractionType.parse(type),
                details,
                Instant.parse(timestamp)
        );
    }
}
