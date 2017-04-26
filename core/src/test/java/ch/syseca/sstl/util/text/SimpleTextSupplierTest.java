package ch.syseca.sstl.util.text;


import static org.junit.Assert.assertEquals;
import java.util.Date;
import org.junit.Test;


public class SimpleTextSupplierTest {

    @Test
    public void testArgMustNotBeNull() throws Exception {
        assertEquals("Argument [v1] must not be null.", SimpleTextSupplier.argMustNotBeNull("v1").get());
    }


    @Test
    public void testDateTimeString() throws Exception {
        final Date d = new Date(0);

        assertEquals("1970-01-01T00:00:00Z", SimpleTextSupplier.dateTimeString(d).get());
    }
}