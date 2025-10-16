package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.ui.CommandHistory;

/**
 * A class to access command history data stored as a JSON file on the hard disk.
 */
public class JsonCommandHistoryStorage implements CommandHistoryStorage {

    private final Path filePath;

    /**
     * Creates a {@code JsonCommandHistoryStorage} with the specified file path.
     *
     * @param filePath The path to the JSON file used to store command history.
     */
    public JsonCommandHistoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getCommandHistoryFilePath() {
        return filePath;
    }

    @Override
    public Optional<CommandHistory> readCommandHistory() throws DataLoadingException {
        return JsonUtil.readJsonFile(filePath, CommandHistory.class);
    }

    @Override
    public void saveCommandHistory(CommandHistory commandHistory) throws IOException {
        JsonUtil.saveJsonFile(commandHistory, filePath);
    }
}
