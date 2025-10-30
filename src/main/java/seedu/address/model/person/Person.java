package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.interaction.Interaction;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements PersonReadOnly {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    private final Role role; // may be null if not provided
    private final Cadence cadence; // may be null if not provided

    private final List<Interaction> interactions;

    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, null, null, List.of());
    }

    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, List<Interaction> interactions) {
        this(name, phone, email, address, tags, null, null, interactions);
    }

    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Role role) {
        this(name, phone, email, address, tags, role, null, List.of());
    }

    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Role role, Cadence cadence) {
        this(name, phone, email, address, tags, role, cadence, List.of());
    }

    /** Constructor */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Role role, Cadence cadence, List<Interaction> interactions) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.role = role; // allowed to be null for legacy callers
        this.cadence = cadence; // allowed to be null for legacy callers
        // defensive copy + immutable view
        this.interactions = Collections.unmodifiableList(
            new ArrayList<>(interactions == null ? List.of() : interactions));
    }

    /** Copy-with constructor for updated interaction history. */
    public Person(Person base, List<Interaction> newInteractions) {
        this(base.name, base.phone, base.email, base.address, base.tags,
            base.role, base.cadence, newInteractions);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Role getRole() {
        return role;
    }

    public java.util.Optional<Cadence> getCadence() {
        return java.util.Optional.ofNullable(cadence);
    }

    /** Immutable view of the interaction history (most recent at the end). */
    public List<Interaction> getInteractions() {
        return interactions;
    }

    /** Last (most recent) interaction, or null if none exists. */
    public Interaction getLastInteractionOrNull() {
        return interactions.isEmpty() ? null : interactions.get(interactions.size() - 1);
    }

    /** Predict next follow-up date (last interaction date + cadence). */
    public LocalDateTime getNextInteractionOrNull() {
        Interaction last = getLastInteractionOrNull();
        if (last == null || cadence == null) {
            return null;
        }

        return LocalDateTime.ofInstant(last.getTimestamp(), ZoneId.systemDefault())
                .plusDays(cadence.getIntervalDays());
    }

    /** Weaker equality: same name. */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null && otherPerson.getName().equals(getName());
    }

    /** Stronger equality: all identity/data fields. */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person o = (Person) other;
        return name.equals(o.name)
            && phone.equals(o.phone)
            && email.equals(o.email)
            && address.equals(o.address)
            && tags.equals(o.tags)
            && Objects.equals(role, o.role)
            && Objects.equals(cadence, o.cadence)
            && interactions.equals(o.interactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags, role, cadence, interactions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("name", name)
            .add("phone", phone)
            .add("email", email)
            .add("address", address)
            .add("tags", tags)
            .add("role", role)
            .add("cadence", cadence)
            .add("interactions(count)", interactions.size())
            .toString();
    }
}
