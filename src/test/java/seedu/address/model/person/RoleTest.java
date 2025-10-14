package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "manager";
        assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void isValidRole() {
        // null role should return false
        assertFalse(Role.isValidRole(null));

        // blank role
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only

        // invalid roles
        assertFalse(Role.isValidRole("boss"));
        assertFalse(Role.isValidRole("partnered"));
        assertFalse(Role.isValidRole("customer service"));
        assertFalse(Role.isValidRole("leadership"));
        assertFalse(Role.isValidRole("unknown"));

        // valid roles (case-insensitive)
        assertTrue(Role.isValidRole("Investor"));
        assertTrue(Role.isValidRole("Partner"));
        assertTrue(Role.isValidRole("Customer"));
        assertTrue(Role.isValidRole("Lead"));
        assertTrue(Role.isValidRole("investor"));
        assertTrue(Role.isValidRole("partner"));
        assertTrue(Role.isValidRole("customer"));
        assertTrue(Role.isValidRole("lead"));
    }

    @Test
    public void equals() {
        Role customerRole = new Role("Customer");

        // same values -> returns true
        assertTrue(customerRole.equals(new Role("customer")));

        // same object -> returns true
        assertTrue(customerRole.equals(customerRole));

        // null -> returns false
        assertFalse(customerRole.equals(null));

        // different types -> returns false
        assertFalse(customerRole.equals(5.0f));

        // different values -> returns false
        assertFalse(customerRole.equals(new Role("Investor")));
    }

    @Test
    public void toString_sameAsValue() {
        Role role = new Role("Investor");
        assertTrue(role.toString().equals("Investor"));
    }
}
