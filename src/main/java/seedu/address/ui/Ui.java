package seedu.address.ui;

import javafx.stage.Stage;
import seedu.address.ui.CommandHistory;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage, CommandHistory commandHistory);

}
