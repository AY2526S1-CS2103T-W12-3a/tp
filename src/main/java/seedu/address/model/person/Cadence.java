package seedu.address.model.person;

/**
 * Represents a follow-up cadence in days.
 */
public class Cadence {

    private final int days;

    public Cadence(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Cadence must be a positive number of days.");
        }
        this.days = days;
    }

    public int getIntervalDays() {
        return days;
    }

    @Override
    public String toString() {
        return String.format("%d days", days);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Cadence
                && days == ((Cadence) other).days);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(days);
    }
}
