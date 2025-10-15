package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Manages the history of user-entered commands.
 */
public class CommandHistory {
    private List<String> history = new ArrayList<>();
    private int pointer = 0;

    /**
     * Creates a {@code CommandHistory} from the given list of commands.
     *
     * @param history List of past commands, may be {@code null}.
     */
    @JsonCreator
    public CommandHistory(@JsonProperty("history") List<String> history) {
        this.history = (history != null) ? new ArrayList<>(history) : new ArrayList<>();
        this.pointer = this.history.size();
    }

    public CommandHistory() {
        this.history = new ArrayList<>();
    }

    public List<String> getHistory() {
        return history;
    }

    /**
     * Adds a command to the history and resets the pointer to the end.
     */
    public void add(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }
        history.add(command);
        pointer = history.size(); // reset pointer to after the last element
    }

    /**
     * Returns the previous command in history if available.
     * Returns null if there is no previous command.
     */
    public String getPrevious() {
        if (history.isEmpty() || pointer == 0) {
            return null;
        }
        pointer--;
        return history.get(pointer);
    }

    /**
     * Returns the next command in history if available.
     * Returns an empty string if at the newest position.
     */
    public String getNext() {
        if (history.isEmpty()) {
            return "";
        }
        if (pointer < history.size() - 1) {
            pointer++;
            return history.get(pointer);
        } else {
            pointer = history.size();
            return "";
        }
    }

    /**
     * Clears all stored history.
     */
    public void clear() {
        history.clear();
        pointer = 0;
    }
}
