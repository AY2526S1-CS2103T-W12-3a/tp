package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.comparator.FollowUpComparator;

/**
 * Sorts all persons by next follow-up based on their last interaction and cadence.
 */
public class SortFollowUpCommand extends Command {

    public static final String COMMAND_WORD = "sortfollowup";
    public static final String MESSAGE_SUCCESS = "Sorted all persons by the next follow-up";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> comparator = new FollowUpComparator();
        model.sortCadenceList(comparator);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
