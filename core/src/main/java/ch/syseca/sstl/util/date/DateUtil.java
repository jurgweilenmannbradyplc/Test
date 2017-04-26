package ch.syseca.sstl.util.date;


import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;


/**
 * Hilfsklasse mit Funktionen auf <code>java.util.Date</code>
 */
@UtilityClass
public final class DateUtil {

    /**
     * Testet, ob zwei {@link Date} gleich sind. Sie sind gleich, wenn beide <code>null</code> sind oder <code>
     * a.getTime() == b.getTime()</code> ist.
     * 
     * @param a Date a.
     * @param b Date b.
     * 
     * @return <code>true</code>, wenn beide Zeiten gleich sind.
     */
    public static boolean equal(Date a, Date b) {
        boolean result;

        if (a == null && b == null) {
            result = true;
        } else if (a != null && b != null) {
            result = a.getTime() == b.getTime();
        } else {
            result = false;
        }

        return result;
    }


    /**
     * Testet, ob zwei {@link Date} unterschiedlich sind. Es ist die Umkehrfunktion von {@link #equal(Date, Date)}:
     * 
     * <pre>
     * notEqual(a, b) == !equal(a, b)
     * </pre>
     * 
     * @param a Date a.
     * @param b Date b.
     * 
     * @return <code>true</code>, wenn beide Zeiten ungleich sind.
     */
    public static boolean notEqual(Date a, Date b) {
        return !equal(a, b);
    }


    /**
     * Ermittelt die Tagesgrenzen (Mitternacht bis Mitternacht) des Tages, in dem <code>time</code> liegt. Zeitzone ist die Default
     * Zeitzone. Die Methode entspricht dem Aufruf
     * 
     * <pre>
     * dayOf(time, TimZone.getDefault())
     * </pre>
     * 
     * @param time Zeitpunkt, dessen Tagesgrenzen ermittelt werden sollen.
     * 
     * @return Tagesgrenzen f&uuml;r den Zeitpunkt <code>time</code>.
     */
    public static TimeRange dayOf(Date time) {
        return dayOf(time, TimeZone.getDefault());
    }


    /**
     * Ermittelt die Tagesgrenzen (Mitternacht bis Mitternacht) des Tages, in dem <code>time</code> liegt.
     * 
     * @param time Zeitpunkt, dessen Tagesgrenzen ermittelt werden sollen.
     * @param timezone Zeitzone, in der die Zeitberechnungen durchgef&uuml;hrt werden.
     * 
     * @return Tagesgrenzen f&uuml;r den Zeitpunkt <code>time</code>.
     */
    public static TimeRange dayOf(Date time, TimeZone timezone) {
        final Calendar c = Calendar.getInstance(timezone);

        c.setTime(time);
        return dayOf(c);
    }


    /**
     * Ermittelt die Tagesgrenzen (Mitternacht bis Mitternacht) des Tages, in dem <code>c.getTime()</code> liegt.
     * 
     * @param c Kalender mit den Zeitinformationen. Achtung: der Kalender wird modifiziert und enth&auml;lt nach dem Aufruf die
     *            Tagesendzeit.
     * 
     * @return Tagesgrenzen f&uuml;r den Zeitpunkt <code>time</code>.
     */
    public static TimeRange dayOf(Calendar c) {
        final Date begin;
        final Date end;

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        begin = c.getTime();

        c.add(Calendar.DAY_OF_MONTH, 1);
        end = c.getTime();

        return new TimeRange(begin, end);
    }


    /**
     * Bestimnmt das Minimum (fruehere) der beiden Zeiten.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return Minimum der beiden Zeiten.
     */
    public static Date min(Date d1, Date d2) {
        return before(d2, d1) ? d2 : d1;
    }


    /**
     * Bestimmt das Maximum (spaetere) der beiden Zeiten.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return Minimum der beiden Zeiten.
     */
    public static Date max(Date d1, Date d2) {
        return after(d2, d1) ? d2 : d1;
    }


    /**
     * Bestimmt, ob <code>d1</code> zeitlich vor <code>d2</code> liegt.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return <code>true</code>, wenn <code>d1</code> zeitlich vor <code>d2</code> liegt.
     */
    public static boolean before(Date d1, Date d2) {
        return d1.getTime() < d2.getTime();
    }


    /**
     * Bestimmt, ob <code>d1</code> zeitlich nach <code>d2</code> liegt.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return <code>true</code>, wenn <code>d1</code> zeitlich nach <code>d2</code> liegt.
     */
    public static boolean after(Date d1, Date d2) {
        return d1.getTime() > d2.getTime();
    }


    /**
     * Bestimmt, ob <code>d1</code> zeitlich nicht vor <code>d2</code> liegt, d.h. d1 &gt;= d2.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return <code>true</code>, wenn <code>d1</code> zeitlich nicht vor <code>d2</code> liegt.
     */
    public static boolean equalOrAfter(Date d1, Date d2) {
        return d1.getTime() >= d2.getTime();
    }


    /**
     * Bestimmt, ob <code>d1</code> zeitlich nicht nach <code>d2</code> liegt, d.h. d1 &lt;= d2;
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2.
     * 
     * @return <code>true</code>, wenn <code>d1</code> zeitlich nicht nach <code>d2</code> liegt.
     */
    public static boolean equalOrBefore(Date d1, Date d2) {
        return d1.getTime() <= d2.getTime();
    }


    /**
     * Vergleicht die beiden Zeiten gemaess {@link java.lang.Comparable}.
     * 
     * @param d1 Zeit 1.
     * @param d2 Zeit 2;
     * 
     * @return Siehe {@link java.lang.Comparable}.
     */
    public static int compare(Date d1, Date d2) {
        int result;

        if (d1 == d2) {
            result = 0;
        } else {
            long t1 = d1.getTime();
            long t2 = d2.getTime();

            if (t1 == t2) {
                result = 0;
            } else if (t1 > t2) {
                result = 1;
            } else {
                result = -1;
            }
        }

        return result;
    }


    /**
     * Addiert or Subtrahiert den angegebenen Wert zu dem gegebenen Kalender-Feld.
     * 
     * @param date Zeit, zu der der Wert addiert werden soll.
     * @param field Kalender-Feld. Siehe {@link Calendar}.
     * @param amount Wert, der addiert werden soll.
     * @param timezone Zeitzone fuer die Zeitberechnung.
     * 
     * @return Neue Zeit.
     * 
     * @see Calendar#add(int, int)
     */
    public static Date add(Date date, int field, int amount, TimeZone timezone) {
        final Calendar c = Calendar.getInstance(timezone);

        c.setTime(date);
        c.add(field, amount);

        return c.getTime();
    }


    public static boolean isOlder(Date date, int amount, int field) {
        return isOlder(date, amount, field, new Date());
    }


    public static boolean isOlder(Date date, int amount, int field, Date referenceTime) {
        return date.getTime() < (referenceTime.getTime() - amount * factorToConvertToMillis(field));
    }


    public static boolean isNotOlder(Date date, int amount, int field) {
        return isNotOlder(date, amount, field, new Date());
    }


    public static boolean isNotOlder(Date date, int amount, int field, Date referenceTime) {
        return !isOlder(date, amount, field, referenceTime);
    }


    private static long factorToConvertToMillis(int field) {
        long result;

        switch (field) {
            case Calendar.MILLISECOND:
                result = 1;
                break;

            case Calendar.SECOND:
                result = 1000;
                break;

            case Calendar.MINUTE:
                result = 60 * 1000;
                break;

            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:
                result = 60 * 60 * 1000;
                break;

            case Calendar.DAY_OF_MONTH:
            case Calendar.DAY_OF_WEEK:
            case Calendar.DAY_OF_WEEK_IN_MONTH:
            case Calendar.DAY_OF_YEAR:
                result = 24 * 60 * 60 * 1000;
                break;

            default:
                throw new IllegalArgumentException(field + " is not a valid number for argument 'field'.");
        }

        return result;
    }


    public static Instant toInstant(Date date) {
        return date != null ? Instant.ofEpochMilli(date.getTime()) : null;
    }
}
