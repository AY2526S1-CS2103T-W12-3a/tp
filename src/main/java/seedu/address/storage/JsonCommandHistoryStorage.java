package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.ui.CommandHistory;

public class JsonCommandHistoryStorage implements CommandHistoryStorage {

    private final Path filePath;

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
