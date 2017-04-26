package ch.syseca.sstl.util.io;


import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Test;


/**
 * JUnit-Test fuer {@link HexOutputStream} und {@link HexInputStream}.
 */
public class HexStreamTest {

    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test
    public void writeToString() throws IOException {
        StringWriter outText = new StringWriter();
        HexOutputStream out = new HexOutputStream(outText);

        out.write(new byte[] {});
        out.flush();
        assertEquals("", outText.getBuffer().toString());

        out.write(new byte[] { (byte) 0x41, (byte) 0x0a, (byte) 0x00, (byte) 0xff });
        out.flush();
        assertEquals("410a00ff", outText.getBuffer().toString());
    }


    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test
    public void readFromString() throws IOException {
        byte[] bytes;
        HexInputStream in;

        in = new HexInputStream(new StringReader("410aff00"));
        bytes = new byte[4];

        assertEquals(4, in.read(bytes));
        assertEquals((byte) 0x41, bytes[0]);
        assertEquals((byte) 0xff, bytes[2]);
        assertEquals((byte) 0x00, bytes[3]);
    }


    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test
    public void writeReadString() throws IOException {
        StringWriter outText = new StringWriter();
        OutputStreamWriter out;
        BufferedReader in;

        out = new OutputStreamWriter(new HexOutputStream(outText));
        out.write("test");
        out.flush();

        in = new BufferedReader(new InputStreamReader(new HexInputStream(new StringReader(outText.toString()))));
        assertEquals("test", in.readLine());

    }


    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test(expected = IOException.class)
    public void readCorrupedString() throws IOException {
        HexInputStream in;

        in = new HexInputStream(new StringReader("410a1"));
        assertEquals(2, in.read(new byte[3]));
        in.read(new byte[3]);
    }


    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test(expected = IOException.class)
    public void readCorrupedString2() throws IOException {
        HexInputStream in;

        in = new HexInputStream(new StringReader("410akj"));
        in.read();
        in.read();
        in.read();
    }


    /**
     * DOCUMENT ME!
     * 
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    @SuppressWarnings("resource")
    @Test
    public void objectSerialisation() throws IOException, ClassNotFoundException {
        Integer value = new Integer(77);
        StringWriter hexText = new StringWriter();
        ObjectOutputStream objOut;
        ObjectInputStream objIn;

        objOut = new ObjectOutputStream(new HexOutputStream(hexText));
        objOut.writeObject(value);
        objOut.flush();

        objIn = new ObjectInputStream(new HexInputStream(new StringReader(hexText.toString())));
        assertEquals(value, objIn.readObject());
    }

}
