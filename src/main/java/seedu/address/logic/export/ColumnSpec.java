package seedu.address.logic.export;

import java.util.function.Function;

/**
 * Describes a single CSV column: its header text and how to extract
 * the string value from a domain object (e.g., PersonReadOnly).
 *
 * Keep this class dumb and generic — it lets the export command stay
 * unaware of concrete fields and future columns.
 */
public final class ColumnSpec<T> {
    public final String header;
    public final Function<T, String> extractor;

    private ColumnSpec(String header, Function<T, String> extractor) {
        this.header = header;
        this.extractor = extractor;
    }

    public static <T> ColumnSpec<T> of(String header, Function<T, String> extractor) {
        return new ColumnSpec<>(header, extractor);
    }
}
