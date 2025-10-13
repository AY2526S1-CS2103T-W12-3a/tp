package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.ui.CommandHistory;
import seedu.address.commons.exceptions.DataLoadingException;

public interface CommandHistoryStorage {

    Path getCommandHistoryFilePath();

    Optional<CommandHistory> readCommandHistory() throws DataLoadingException;

    void saveCommandHistory(CommandHistory commandHistory) throws IOException;
}
