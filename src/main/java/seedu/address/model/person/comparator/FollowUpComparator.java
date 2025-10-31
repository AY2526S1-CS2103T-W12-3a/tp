package seedu.address.model.person.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * Comparator for {@link Person} objects based on their next scheduled follow-up date.
 * <p>
 * Persons with earlier next interaction dates are considered "smaller" and will appear
 * earlier in a sorted list. Persons with no upcoming interactions are considered "larger"
 * and will appear at the end of the list.
 */
public class FollowUpComparator implements Comparator<Person> {

    /**
     * Compares two {@code Person} objects by their next follow-up interaction date.
     *
     * @param p1 the first person to compare
     * @param p2 the second person to compare
     * @return a negative integer, zero, or a positive integer as the first person's next
     *         interaction date is earlier than, equal to, or later than the second person's
     *         next interaction date
     */
    @Override
    public int compare(Person p1, Person p2) {
        LocalDateTime t1 = p1.getNextInteractionOrNull();
        LocalDateTime t2 = p2.getNextInteractionOrNull();

        if (t1 == null && t2 == null) {
            return 0;
        }
        if (t1 == null) {
            return 1;
        }
        if (t2 == null) {
            return -1;
        }
        // Sort by next contact date (earliest first)
        return t1.compareTo(t2);
    }
}
