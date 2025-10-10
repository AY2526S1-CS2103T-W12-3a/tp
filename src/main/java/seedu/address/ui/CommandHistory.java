package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

//Stores and navigates through user commands
public class CommandHistory {
    private final List<String> history = new ArrayList<>();
    private int currentIndex = 0;

    public void add(String command) {
        if (command == null || command.isBlank()) {
            return;
        }
        history.add(command);
        currentIndex = history.size(); //reset pointer to after last
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public boolean hasNext() {
        return currentIndex < history.size() - 1;
    }

    public String getPrevious() {
        if (hasPrevious()) {
            currentIndex--;
            return history.get(currentIndex);
        }
        return "";
    }

    public String getNext() {
        if (hasNext()) {
            currentIndex++;
            return history.get(currentIndex);
        }
        currentIndex = history.size();
        return "";
    }

    public void resetIndex() {
        currentIndex = history.size();
    }
}