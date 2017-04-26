package ch.syseca.sstl.util.beanutil.converter;

import static org.junit.Assert.*;
import java.util.TimeZone;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.junit.Test;


/**
 * JUnit Test f&uuml;r Klasse {@link TimeZoneConverter}.
 */
public class TimeZoneConverterTest {

    /**
     * Test.
     */
    @Test
    public void testConvert() {
        final Converter tzc = new TimeZoneConverter();
        TimeZone tz;

        tz = (TimeZone) tzc.convert(TimeZone.class, "Europe/Zurich");
        assertNotNull(tz);

    }

    /**
     * Test.
     */
    @Test
    public void testWrongClass() {
        Converter c = new TimeZoneConverter();

        try {
            c.convert(String.class, "TEST_2");
            fail("Must throw a ConversionException.");
        } catch (ConversionException e) {
            e.getClass();
            // ok
        }

    }

}
