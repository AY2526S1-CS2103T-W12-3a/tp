package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s name, role, or tags match any of the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {

    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String role = person.getRole() == null ? "" : person.getRole().value.toLowerCase();

        // combine tags into a single string
        String tags = person.getTags().stream()
                .map(tag -> tag.tagName.toLowerCase())
                .reduce("", (a, b) -> a + " " + b);

        return keywords.stream()
                .map(String::toLowerCase)
                .anyMatch(keyword ->
                        name.contains(keyword)
                                || role.contains(keyword)
                                || tags.contains(keyword)
                );
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PersonContainsKeywordsPredicate
                && keywords.equals(((PersonContainsKeywordsPredicate) other).keywords));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .toString();
    }
}
