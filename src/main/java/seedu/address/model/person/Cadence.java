package seedu.address.model.person;

/**
 * Represents a follow-up cadence in days.
 */
public class Cadence {

    private final int days;

    /**
     * Constructor for cadence
     * @param days Number of days between each contact
     */
    public Cadence(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Cadence must be a positive number of days.");
        }
        this.days = days;
    }

    /**
     * Returns integer number of days in cadence
     * @return Integer number of days in cadence
     */
    public int getIntervalDays() {
        return days;
    }

    /**
     * Returns string number of days in cadence
     * @return String number of days in cadence
     */
    @Override
    public String toString() {
        return String.format("%d days", days);
    }

    /**
     * Checks if a cadence object is equal to another cadence object
     * @param other Another cadence object
     * @return true if both cadences are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Cadence
                && days == ((Cadence) other).days);
    }

    /**
     * Returns the hash code value for days
     * @return The hash code value for days
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(days);
    }
}
