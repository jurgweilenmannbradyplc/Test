package ch.syseca.sstl.util.text;


import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import org.junit.Test;


/**
 * JUnit Test f&uuml;r {@link LinkedPropertiesFormat}
 */
public class LinkedPropertiesFormatTest {

    private final LinkedPropertiesFormat pf = new LinkedPropertiesFormat();


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#parseProperties(String)}.
     * 
     * @throws Exception Exception.
     */
    @Test
    public void testParseProperties1() throws Exception {
        LinkedProperties p;

        p = pf.parseProperties("a=3;b=56;");
        assertEquals(2, p.size());
        assertEquals("3", p.get("a"));
        assertEquals("56", p.get("b"));
    }


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#parseProperties(String)}.
     * 
     * @throws Exception Exception.
     */
    @Test
    public void testParseProperties2() throws Exception {
        LinkedProperties p;

        p = pf.parseProperties("");
        assertEquals(0, p.size());

        p = pf.parseProperties("       ");
        assertEquals(0, p.size());

        p = pf.parseProperties(null);
        assertEquals(0, p.size());
    }


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#parseProperties(String)}.
     * 
     * @throws Exception Exception.
     */
    @Test
    public void testParseProperties3() throws Exception {
        LinkedProperties p;

        p = pf.parseProperties("a=2+2==4;b=56;;;c=9");
        assertEquals(3, p.size());
        assertEquals("2+2=4", p.get("a"));
        assertEquals("56;", p.get("b"));
        assertEquals("9", p.get("c"));
    }


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#format(Object)}
     */
    @Test
    public void testFormat() {
        LinkedProperties p = new LinkedProperties();

        p.put("a", "4");
        assertEquals("a=4;", pf.format(p));

        p.put("a", "1+2=3;");
        assertEquals("a=1+2==3;;;", pf.format(p));
    }


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#parseProperties(String)}.
     * 
     * @throws Exception Exception.
     */
    @Test
    public void testParsePropertiesError() throws Exception {

        try {
            pf.parseProperties("= ; a = 1;     ");
            throw new AssertionError();
        } catch (ParseException e) {

            // ok
            e.getClass();
        }
    }


    /**
     * Test f&uuml;r {@link LinkedPropertiesFormat#parseProperties(String)}.
     * 
     * @throws Exception Exception.
     */
    @Test
    public void testParsePropertiesTrailingBlanks() throws Exception {
        LinkedProperties p;

        p = pf.parseProperties("a= 1  ;   b= 2 ;  ");
        assertEquals(2, p.size());
        assertEquals("1", p.get("a"));
        assertEquals("2", p.get("b"));
    }


    @Test
    public void testOrder() throws Exception {
        LinkedProperties p = new LinkedProperties();
        StringBuilder expectedResult = new StringBuilder();
        int index;

        for (index = 0; index < 100; index++) {
            String k = "k" + index;
            String v = "v" + index;

            p.put(k, v);
            expectedResult.append(k).append("=").append(v).append(";");
        }
        assertEquals(expectedResult.toString(), pf.format(p));

        p = pf.parseProperties(expectedResult.toString());

        index = 0;
        for (String k : p.keySet()) {
            assertEquals("k" + index, k);
            index++;
        }

    }

}
