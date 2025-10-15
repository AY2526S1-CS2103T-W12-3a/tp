package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.ui.CommandHistory;

/**
 * Represents a storage for {@link CommandHistory}.
 * Handles reading and writing of command history data from and to the local file system.
 */
public interface CommandHistoryStorage {

    /**
     * Returns the file path of the command history data file.
     *
     * @return Path object representing the command history file location.
     */
    Path getCommandHistoryFilePath();


    /**
     * Returns the command history data as an {@link Optional}.
     * Returns {@code Optional.empty()} if the file is not found.
     *
     * @throws DataLoadingException if there was any problem reading data from storage.
     */
    Optional<CommandHistory> readCommandHistory() throws DataLoadingException;


    /**
     * Saves the given {@link CommandHistory} to the storage file.
     *
     * @param commandHistory The command history data to save. Must not be {@code null}.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCommandHistory(CommandHistory commandHistory) throws IOException;
}
