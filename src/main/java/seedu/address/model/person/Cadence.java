package seedu.address.model.person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a follow-up cadence for a contact.
 * Each cadence defines when the next follow-up should occur
 * based on a set interval (e.g., every 7 days).
 */
public class Cadence {

    private final int intervalDays; // e.g., 7 days, 14 days, etc.
    private LocalDate lastContacted;
    private LocalDate nextContactDate;

    /**
     * Constructs a Cadence with the given interval in days.
     *
     * @param intervalDays the number of days between follow-ups
     * @param lastContacted the last contact date
     */
    public Cadence(int intervalDays, LocalDate lastContacted) {
        if (intervalDays <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        this.intervalDays = intervalDays;
        this.lastContacted = lastContacted;
        this.nextContactDate = lastContacted.plusDays(intervalDays);
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public LocalDate getLastContacted() {
        return lastContacted;
    }

    public LocalDate getNextContactDate() {
        return nextContactDate;
    }

    /**
     * Marks a follow-up as done today and recalculates the next contact date.
     */
    public void markFollowUpDone() {
        this.lastContacted = LocalDate.now();
        this.nextContactDate = this.lastContacted.plusDays(intervalDays);
    }

    /**
     * Returns true if the next follow-up date is today or earlier.
     */
    public boolean isDue() {
        return !LocalDate.now().isBefore(nextContactDate);
    }

    /**
     * Returns how many days until the next follow-up.
     */
    public long daysUntilNextFollowUp() {
        return ChronoUnit.DAYS.between(LocalDate.now(), nextContactDate);
    }

    @Override
    public String toString() {
        return String.format("Cadence: every %d days | next follow-up: %s",
                intervalDays, nextContactDate);
    }
}
