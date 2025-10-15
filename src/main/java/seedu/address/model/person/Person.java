package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

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
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    // Interaction history (most recent at the end)
    private final List<Interaction> interactions;

    /**
     * Backward-compatible constructor: every original field must be present and not null.
     * Initializes with an empty interaction history.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, List.of());
    }

    /**
     * Full constructor including interactions.
     * Every field must be present and not null (interactions may be empty).
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, List<Interaction> interactions) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        // Defensively copy and make immutable view
        this.interactions = Collections.unmodifiableList(
                new ArrayList<>(interactions == null ? List.of() : interactions));
    }

    /**
     * Copy-with constructor to create an updated Person sharing all fields but a new interaction list.
     */
    public Person(Person base, List<Interaction> newInteractions) {
        this(base.name, base.phone, base.email, base.address, base.tags, newInteractions);
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

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable view of the interaction history (most recent at the end).
     */
    public List<Interaction> getInteractions() {
        return interactions;
    }

    /**
     * Returns the last (most recent) interaction, or null if none exists.
     */
    public Interaction getLastInteractionOrNull() {
        return interactions.isEmpty() ? null : interactions.get(interactions.size() - 1);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && interactions.equals(otherPerson.interactions);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, interactions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                // Keep history concise in logs; UI shows full details where needed
                .add("interactions(count)", interactions.size())
                .toString();
    }

}
