package ch.syseca.sstl.util.text;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * JUnit-Test f&uuml;r {@link WildcardSearch}.
 */
public class WildcardSearchTest {

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testException() {
        try {
            assertTrue(WildcardSearch.getInstance().matches(null, "T*"));
            throw new AssertionError("Must throw an IllegalArgumentException exception.");
        } catch (IllegalArgumentException e) {
            e.getClass(); // ok
        }

        try {
            assertTrue(WildcardSearch.getInstance().matches("Test", null));
            throw new AssertionError("Must throw an IllegalArgumentException exception.");
        } catch (IllegalArgumentException e) {
            e.getClass(); // ok
        }

        try {
            assertTrue(WildcardSearch.getInstance().matches(null, null));
            throw new AssertionError("Must throw an IllegalArgumentException exception.");
        } catch (IllegalArgumentException e) {
            e.getClass(); // ok
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testMatches() {
        assertTrue(WildcardSearch.getInstance().matches("Test", "T*"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "t*"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "*e*"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "*"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "t_st"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "T_s*"));

        assertFalse(WildcardSearch.getInstance().matches("Test", "*x*"));

        assertTrue(WildcardSearch.getInstance().matches("T*st", "T*"));
        assertFalse(WildcardSearch.getInstance().matches("Test", "T\\*st"));
        assertTrue(WildcardSearch.getInstance().matches("T*st", "T\\*st"));

        assertTrue(WildcardSearch.getInstance().matches("T_st", "T\\_st"));
        assertTrue(WildcardSearch.getInstance().matches("Test", "T_st"));
        assertFalse(WildcardSearch.getInstance().matches("Test", "T\\_st"));
    }

}
