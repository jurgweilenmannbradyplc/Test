package ch.syseca.sstl.util.beanutil;

import java.util.TimeZone;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision: 934 $, $Date: 2009-09-10 12:32:34 +0200 (Do, 10 Sep 2009) $
 */
public class Foo {

    /**
     * DOCUMENT ME!
     *
     * @author   $author$
     * @version  $Revision: 934 $, $Date: 2009-09-10 12:32:34 +0200 (Do, 10 Sep 2009) $
     */
    public static enum FooType { TYPE_1, TYPE_2 }


    private int value;
    private TimeZone timezone;
    private FooType fooType;

    /**
     * Creates a new Foo object.
     */
    public Foo() {
        isPrivate();
    }

    /**
     * Retrieves the value of
     *
     * @return  DOCUMENT ME!
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of $param.name$
     *
     * @param  value  DOCUMENT ME!
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Retrieves the value of
     *
     * @return  DOCUMENT ME!
     */
    public TimeZone getTimezone() {
        return timezone;
    }

    /**
     * Sets the value of $param.name$
     *
     * @param  timezone  DOCUMENT ME!
     */
    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    /**
     * Gibt den Wert des Feldes <code>fooType</code> zurück.
     *
     * @return  Wert des Feldes <code>fooType</code>.
     *
     * @see     #fooType
     */
    public FooType getFooType() {
        return fooType;
    }

    /**
     * Setzt einen neuen Wert für das Feld <code>fooType</code>.
     *
     * @param  fooType  Neuer Wert für das Feld <code>fooType</code>.
     *
     * @see    #fooType
     */
    public void setFooType(FooType fooType) {
        this.fooType = fooType;
    }

    /**
     * Retrieves the value of
     *
     * @return  DOCUMENT ME!
     */
    private boolean isPrivate() {
        return true;
    }
}
