package ch.syseca.sstl.util.text;

import junit.framework.TestCase;


/**
 * This class tests the formatting functionality of <code>RhuDecimalFormat</code>.
 */
public class RhuDecimalFormatTest extends TestCase {

    /**
     * @param  pattern  formatting pattern
     */
    public RhuDecimalFormatTest(String pattern) {
        super(pattern);
    }

    /**
     * Executed before every single test
     *
     * @throws  Exception  if exception
     *
     * @see     junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test method for {@link ch.syseca.sstl.util.text.RhuDecimalFormat#RhuDecimalFormat(java.lang.String)}.
     */
    public void testRhuDecimalFormat() {
        RhuDecimalFormat df = new RhuDecimalFormat("####00.00");
        assertNotNull(df);
        assertTrue(df instanceof RhuDecimalFormat);
    }

    /**
     * Test method for {@link ch.syseca.sstl.util.text.RhuDecimalFormat#format(double, java.lang.StringBuffer,
     * java.text.FieldPosition)}.
     */
    public void testFormatDoubleStringBufferFieldPosition() {

        final RhuDecimalFormat df1 = new RhuDecimalFormat("#");
        assertEquals("1", df1.format(1.1));   // round down to 1
        assertEquals("12", df1.format(12.1)); // round down to 12
        assertEquals("1", df1.format(1.4));   // round down to 1
        assertEquals("2", df1.format(1.5));   // round up to 2
        assertEquals("-1", df1.format(-1.4)); // round down to -1
        assertEquals("-2", df1.format(-1.5)); // round dotn to -2

        final RhuDecimalFormat df2 = new RhuDecimalFormat("#.#");
        assertEquals("1.1", df2.format(1.149));             // round down to 1.1
        assertEquals("1.2", df2.format(1.15));              // round up to 1.2
        assertEquals("1.3", df2.format(1.25));              // round up to 1.3
        assertEquals("77777.1", df2.format(77777.149));     // round down to 12.1
        assertEquals("77777.1", df2.format(77777.1499999)); // round up to 77777.2
        assertEquals("-1.1", df2.format(-1.149));           // round down to -1.1
        assertEquals("-1.2", df2.format(-1.15));            // round up to -1.2
        assertEquals("-77777.1", df2.format(-77777.149));   // round down to -12.1
        assertEquals("-77777.1", df2.format(-77777.149));   // round up to -77777.2

        final RhuDecimalFormat df3 = new RhuDecimalFormat("####00.0000");
        assertEquals("01.1235", df3.format(1.12345));   // leading zero, round up to 01.1235
        assertEquals("-01.1235", df3.format(-1.12345)); // leading zero, round up to -01.1235

        RhuDecimalFormat df4 = new RhuDecimalFormat("####00.000");
        assertEquals("12345.003", df4.format(12345.0025));   // round up to 12345.003 -> uneven
        assertEquals("12345.004", df4.format(12345.0035));   // round up to 12345.004 -> even
        assertEquals("-12345.003", df4.format(-12345.0025)); // round up to -12345.003 -> uneven
        assertEquals("-12345.004", df4.format(-12345.0035)); // round up to -12345.004 -> even

        // problems we had with DecimalFormat in EDIS
        RhuDecimalFormat df5 = new RhuDecimalFormat("###,##0.0##;-###,##0.0##");
        final double test5a = (0.001d + 0.002d + 0.003d + 0.004d) / 4;
        assertEquals("0.003", df5.format(test5a));
        assertEquals("-0.003", df5.format(-1 * test5a));
        final double test5b = (175.001d + 175.002d + 175.003d + 175.004d) / 4;
        assertEquals("175.003", df5.format(test5b));
        assertEquals("-175.003", df5.format(-1 * test5b));

        RhuDecimalFormat df6 = new RhuDecimalFormat("###,##0.0###;-###,##0.0###");
        final double test6a = (0.0001d + 0.0002d + 0.0003d + 0.0004d) / 4;
        assertEquals("0.0003", df6.format(test6a));
        assertEquals("-0.0003", df6.format(-1 * test6a));
        final double test6b = (175.0001d + 175.0002d + 175.0003d + 175.0004d) / 4;
        assertEquals("175.0003", df6.format(test6b));
        assertEquals("-175.0003", df6.format(-1 * test6b));
    }

}
