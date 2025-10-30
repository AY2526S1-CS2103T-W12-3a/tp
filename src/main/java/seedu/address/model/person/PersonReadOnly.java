package seedu.address.model.person;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.interaction.Interaction;
import seedu.address.model.tag.Tag;

/**
 * Read-only view of a {@link Person}.
 * Implementations must not expose mutable state.
 */
public interface PersonReadOnly {
    Name getName();
    Phone getPhone();
    Email getEmail();
    Address getAddress();
    Set<Tag> getTags();
    Role getRole();
    Optional<Cadence> getCadence();
    List<Interaction> getInteractions();
}
