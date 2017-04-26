package ch.syseca.sstl.util.io;


import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackReader;
import java.io.Reader;


/**
 * Input-Stream, der Daten als <code>byte</code> liest, die als Hexadezimal-Text kodiert sind. Pro <code>byte</code>
 * werden zwei Character eingelesen.
 *
 * @see  HexOutputStream
 */
public class HexInputStream extends InputStream {

    private final PushbackReader in;


    /**
     * Erzeugt einen neuen Stream, der die Hex-kodierten Zeichen aus einem {@link Reader} liest.
     *
     * @param  in  Reader, aus dem die Daten gelesen werden.
     */
    public HexInputStream(Reader in) {

        // da wir immer 2 Zeichen benoetigen, koennte es vorkommen, dass der Reader nur noch 1 Zeichen liefert.
        // In diesem Fall wird das zuviel gelesene Zeichen wieder zurueckgestellt.
        if (in instanceof PushbackReader) {
            this.in = (PushbackReader) in;
        } else {
            this.in = new PushbackReader(in, 2);
        }
    }


    @Override
    public int read() throws IOException {
        int val0, val1;
        char c0, c1;
        int result;

        // wir brauchen 2 chars
        result = this.in.read();
        if (result == -1) {
            return result;
        }
        c0 = (char) result;
        val0 = Character.digit(c0, 16);
        if (val0 == -1) {
            this.in.unread(c0);
            throw new CharConversionException(
                    "Corrupted hex stream. " + c0 + " is not a valid hex char.");
        }

        result = this.in.read();
        if (result == -1) {
            this.in.unread(c0);
            throw new IOException("Corrupted hex stream. Missing chars.");
        }
        c1 = (char) result;
        val1 = Character.digit(c1, 16);
        if (val1 == -1) {
            this.in.unread(c1);
            this.in.unread(c0);
            throw new CharConversionException(
                    "Corrupted hex stream. " + c1 + " is not a valid hex char.");
        }

        result = val0 * 16 + val1;

        return result;
    }

}
