package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.comparator.CadenceComparator;

/**
 * Sorts all persons by next contact date of their cadence.
 */
public class SortCadenceCommand extends Command {

    public static final String COMMAND_WORD = "sortcadence";
    public static final String MESSAGE_SUCCESS = "Sorted all persons by next contact date.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> comparator = new CadenceComparator();
        model.sortCadenceList(comparator);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
