package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    // Add history tracking fields
    private final List<String> commandHistory = new ArrayList<>();
    private int historyPointer = 0;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        // handle up/down arrow navigation
        commandTextField.setOnKeyPressed(event -> {
           if (event.getCode() == KeyCode.UP) {
               navigateToPreviousCommand();
               event.consume();
           } else if (event.getCode() == KeyCode.DOWN) {
               navigateToNextCommand();
               event.consume();
           }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);

            commandHistory.add(commandText);
            historyPointer = commandHistory.size();
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    // Helper methods for navigating history
    private void navigateToPreviousCommand() {
        if (commandHistory.isEmpty()) {
            return;
        }
        if (historyPointer > 0) {
            historyPointer--;
            commandTextField.setText(commandHistory.get(historyPointer));
            commandTextField.positionCaret(commandTextField.getText().length());
        }
    }

    private void navigateToNextCommand() {
        if (commandHistory.isEmpty()) {
            return;
        }
        if (historyPointer < commandHistory.size() - 1) {
            historyPointer++;
            commandTextField.setText(commandHistory.get(historyPointer));
        } else {
            // If already at latest, clear field
            historyPointer = commandHistory.size();
            commandTextField.clear();
        }
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
