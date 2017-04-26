package ch.syseca.sstl.util.misc;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * JUnit Test f&uuml;r <code>Severity</code>.
 */
public class SeverityTest {

    @Test
    public void testWorseThan() {
        assertTrue(Severity.WARN.worseThan(Severity.INFO));
        assertTrue(Severity.ERROR.worseThan(Severity.INFO));
        assertTrue(Severity.ERROR.worseThan(Severity.WARN));
    }


    @Test
    public void testWorstOf() {
        assertEquals(Severity.WARN, Severity.worstOf(Severity.INFO, Severity.WARN));
        assertEquals(Severity.WARN, Severity.worstOf(Severity.WARN, Severity.INFO));
        assertEquals(Severity.WARN, Severity.worstOf(Severity.INFO, Severity.WARN, Severity.INFO, Severity.INFO));
        assertEquals(Severity.ERROR, Severity.worstOf(Severity.INFO, Severity.WARN, Severity.ERROR, Severity.WARN));
        assertEquals(Severity.FATAL, Severity.worstOf(Severity.FATAL, Severity.WARN, Severity.ERROR, Severity.WARN));
    }
}
