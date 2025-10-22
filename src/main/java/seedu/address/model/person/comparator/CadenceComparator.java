package seedu.address.model.person.comparator;

import java.util.Comparator;

import seedu.address.model.interaction.Interaction;
import seedu.address.model.person.Person;

/**
 * Comparator for {@link Person} objects based on the timestamp of their last {@link Interaction}.
 * <p>
 * Persons with more recent last interactions are considered "smaller" and come first
 * in a sorted list. Persons with no interactions are considered "larger" and
 * will appear at the end of the list.
 */
public class CadenceComparator implements Comparator<Person> {

    /**
     * Compares two {@code Person} objects by the timestamp of their most recent interaction.
     *
     * @param p1 the first person to compare
     * @param p2 the second person to compare
     * @return a negative integer, zero, or a positive integer as the first person's last interaction
     *         is more recent, equal to, or less recent than the second person's last interaction
     */
    @Override
    public int compare(Person p1, Person p2) {
        Interaction i1 = p1.getLastInteractionOrNull();
        Interaction i2 = p2.getLastInteractionOrNull();

        if (i1 == null && i2 == null) {
            return 0;
        }
        if (i1 == null) {
            return 1;
        }
        if (i2 == null) {
            return -1;
        }

        // Sort by last contact date (earliest first)
        return i2.getTimestamp().compareTo(i1.getTimestamp());
    }
}
