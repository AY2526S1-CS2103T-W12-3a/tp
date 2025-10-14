package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.ui.CommandHistory;

public class JsonCommandHistoryStorageTest {

    @TempDir
    public Path testFolder;

    private Path filePath;
    private JsonCommandHistoryStorage storage;

    @BeforeEach
    public void setUp() {
        filePath = testFolder.resolve("commandHistory.json");
        storage = new JsonCommandHistoryStorage(filePath);
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        Optional<CommandHistory> history = storage.readCommandHistory();
        assertFalse(history.isPresent(), "Expected no history file to be found");
    }

    @Test
    public void saveAndReadCommandHistory_success() throws IOException, DataLoadingException {
        CommandHistory original = new CommandHistory();
        original.add("list");
        original.add("add John Doe p/98765432 e/johnd@example.com");

        // Save
        storage.saveCommandHistory(original);

        // Read back
        Optional<CommandHistory> readBack = storage.readCommandHistory();

        assertEquals(original.getHistory(), readBack.get().getHistory());
    }

    @Test
    public void saveCommandHistory_null_throwsNullPointerException() {
        try {
            storage.saveCommandHistory(null);
        } catch (NullPointerException e) {
            // expected
        } catch (Exception e) {
            throw new AssertionError("Expected NullPointerException, got " + e);
        }
    }

    @Test
    public void read_invalidJson_throwsDataLoadingException() throws IOException {
        // Write an invalid JSON file manually
        Files.writeString(filePath, "not a valid json");

        try {
            storage.readCommandHistory();
        } catch (DataLoadingException e) {
            // expected
            return;
        }
        throw new AssertionError("Expected DataLoadingException for invalid JSON");
    }

    @Test
    public void saveCommandHistory_ioException_throwsIoException() {
        Path invalidPath = Paths.get("/invalid/path/CommandHistory.json");
        JsonCommandHistoryStorage storage = new JsonCommandHistoryStorage(invalidPath);
        CommandHistory history = new CommandHistory();
        assertThrows(IOException.class, () -> storage.saveCommandHistory(history));
    }
}
