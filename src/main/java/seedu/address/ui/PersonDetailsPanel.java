package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.interaction.Interaction;
import seedu.address.model.person.Person;

/**
 * UI panel that displays the full details of the selected {@link seedu.address.model.person.Person},
 * including their interactions list.
 */
public class PersonDetailsPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailsPanel.fxml";
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private Label header;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
    @FXML
    private Label role;
    @FXML
    private FlowPane tags;
    @FXML
    private ListView<Interaction> interactionList;
    @FXML
    private Label emptyHint;

    /**
     * Creates the details panel that shows a selected person's full information,
     * including interactions. Loads the FXML and sets up cell renderers.
     */
    public PersonDetailsPanel() {
        super(FXML);
        setupInteractionCells();
        showEmpty();
    }

    private void setupInteractionCells() {
        interactionList.setCellFactory(list -> new ListCell<Interaction>() {
            @Override
            protected void updateItem(Interaction it, boolean empty) {
                super.updateItem(it, empty);
                if (empty || it == null) {
                    setText(null);
                } else {
                    // Interaction has getType(), getDetails(), getTimestamp()
                    String ts = TS_FMT.format(it.getTimestamp().atZone(java.time.ZoneId.systemDefault()));
                    setText(String.format("[%s] %s — %s", ts, it.getType(), it.getDetails()));
                }
            }
        });
    }

    /**
     * Updates the panel to display details of the given person.
     *
     * @param person the selected person whose details should be shown; may be {@code null}
     *               to clear the panel when no person is selected
     */
    public void setPerson(Person person) {
        if (person == null) {
            showEmpty();
            return;
        }
        emptyHint.setVisible(false);
        header.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);
        var r = person.getRole();
        role.setText(r == null ? "" : r.value);

        tags.getChildren().clear();
        person.getTags().stream()
            .sorted(Comparator.comparing(tag -> tag.tagName))
            .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        interactionList.getItems().setAll(person.getInteractions());
    }

    private void showEmpty() {
        header.setText("Contact details");
        phone.setText("-");
        email.setText("-");
        address.setText("-");
        role.setText("-");
        tags.getChildren().clear();
        interactionList.getItems().clear();
        emptyHint.setVisible(true);
    }
}
