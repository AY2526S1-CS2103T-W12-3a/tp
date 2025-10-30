package seedu.address.logic.export;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.PersonReadOnly;

/**
 * Preset, extensible column schemas for CSV export.
 * Add/remove columns here without touching command code.
 */
public final class ExportSchemas {
    private ExportSchemas() {}

    /** Current behaviour: 5 columns. */
    public static List<ColumnSpec<PersonReadOnly>> standard() {
        return List.of(
                ColumnSpec.of("Name", p -> p.getName().toString()),
                ColumnSpec.of("Phone", p -> p.getPhone().toString()),
                ColumnSpec.of("Email", p -> p.getEmail().toString()),
                ColumnSpec.of("Address", p -> p.getAddress().toString()),
                ColumnSpec.of("Tags", p -> p.getTags().stream()
                        .map(Object::toString).collect(Collectors.joining(";")))
        );
    }

    /** Future-ready: adds Role, Cadence, Interactions count. */
    public static List<ColumnSpec<PersonReadOnly>> full() {
        return List.of(
                ColumnSpec.of("Name", p -> p.getName().toString()),
                ColumnSpec.of("Phone", p -> p.getPhone().toString()),
                ColumnSpec.of("Email", p -> p.getEmail().toString()),
                ColumnSpec.of("Address", p -> p.getAddress().toString()),
                ColumnSpec.of("Tags", p -> p.getTags().stream()
                        .map(Object::toString).collect(Collectors.joining(";"))),
                ColumnSpec.of("Role", p -> p.getRole() == null ? "" : p.getRole().toString()),
                ColumnSpec.of("Cadence", p -> p.getCadence().map(Object::toString).orElse("")),
                ColumnSpec.of("Interactions", p -> Integer.toString(p.getInteractions().size()))
        );
    }
}
