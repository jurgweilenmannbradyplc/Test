package ch.syseca.sstl.util.io;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;


/**
 * Dieser Stream kodiert ein <code>byte</code> als Hexadezimaler Text. Pro <code>byte</code> werden 2 Zeichen erzeugt.
 * Z.B. wird das <code>byte</code> mit Wert <code>0x41</code> (entspricht ASCII <code>A</code>) als String <code>
 * 30</code> ausgegeben.
 *
 * <p>Dieser Stream kann hilfreich sein, wenn z.B. binaere Daten ueber einen Kanal verschickt werden muessen, der
 * Character orientiert ist. Ein serialisiertes Objekt kann z.B. nicht direkt ueber die <code>
 * in</code>/<code>out</code>-Streams zwischen 2 Prozessen ausgetauscht werden.</p>
 *
 * <p>Das folgende Beispiel funktioniert nicht:</p>
 *
 * <p>Prozess 1 (Parent-Prozess):</p>
 *
 * <pre>
 *          ObjectOutputStream objOut;
 *          Process reader;
 *
 *          ...
 *          ...
 *          objOut = new ObjectOutputStream(reader.getOutputStream());
 *          objOut.writeObject(new Integer(77));
 *          objOut.flush();
 *          ...
 *          ...
 * </pre>
 *
 * <p>Prozess 2 (Child-Prozess):</p>
 *
 * <pre>
 *          ObjectInputStream objIn;
 *          Object value;
 *
 *          ...
 *          ...
 *          objIn = new ObjectInputStream(System.in);
 *          value = objIn.readObject();
 *          ...
 *          ...
 * </pre>
 *
 * <p>Wird das serialisierte Objekt jedoch zuerst in einen Hex-String gewandelt, werden die Daten korrekt uebertragen:
 * </p>
 *
 * <p>Prozess 1 (Parent-Prozess):</p>
 *
 * <pre>
 *          ObjectOutputStream objOut;
 *          Process reader;
 *
 *          ...
 *          ...
 *          objOut = new ObjectOutputStream(new HexOutputStream(new OutputStreamWriter(reader.getOutputStream())));
 *          objOut.writeObject(new Integer(77));
 *          objOut.flush();
 *          ...
 *          ...
 * </pre>
 *
 * <p>Prozess 2 (Child-Prozess):</p>
 *
 * <pre>
 *          ObjectInputStream objIn;
 *          Object value;
 *
 *          ...
 *          ...
 *          objIn = new ObjectInputStream(new HexInputStream(new InputStreamReader(System.in)));
 *          value = objIn.readObject();
 *          ...
 *          ...
 * </pre>
 *
 * @see  HexInputStream
 */
public class HexOutputStream extends OutputStream {

    private final Writer out;


    /**
     * Creates a new HexByteOutputStream object.
     *
     * @param  out  DOCUMENT ME!
     */
    public HexOutputStream(Writer out) {
        this.out = out;
    }


    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        writeHex(bytes, off, len);
    }


    @Override
    public void write(byte[] bytes) throws IOException {
        writeHex(bytes, 0, bytes.length);
    }


    @Override
    public void write(int b) throws IOException {
        writeHex(new byte[] { (byte) b }, 0, 1);
    }


    @Override
    public void close() throws IOException {
        super.close();
        this.out.close();
    }


    @Override
    public void flush() throws IOException {
        super.flush();
        this.out.flush();
    }


    protected void writeHex(byte[] bytes, int off, int len) throws IOException {
        final StringBuffer hexText = new StringBuffer(2 * len);

        for (int i = off; i < len; i++) {
            int val = 0xff & bytes[i];
            String t = Integer.toHexString(val);

            if (t.length() == 1) {
                hexText.append("0");
            }
            hexText.append(t);
        }

        this.out.write(hexText.toString());
    }

}
