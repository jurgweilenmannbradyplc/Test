package ch.syseca.sstl.util.date;


import java.io.Serializable;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ch.syseca.sstl.util.text.SimpleTextSupplier;
import lombok.Builder;
import lombok.Getter;


/**
 * Daststellung eines Zeitbereiches bestehen aus einer Anfangszeit und einer Endzeit. Ein Zeitpunkt liegt im
 * Zeitbereich, wenn
 *
 * <pre>
   Beginn-Zeit &lt;= Zeitpunkt &lt; End-Zeit
 * </pre>
 *
 * <p>ist.</p>
 */
public final class TimeRange implements Serializable {

    /** Anfangszeit (inklusive). */
    @Getter
    private final Date beginTime;

    /** Endzeit (exklusive). */
    @Getter
    private final Date endTime;

    /** Long-Bereich, auf den der Zeitbereich abgebildet wird. */
    private transient Range<Long> longRange;


    /**
     * Default-Konstruktor. Kann z.B. von Hibernate verwendet werden.
     */
    @SuppressWarnings("unused")
    private TimeRange() {
        this.beginTime = null;
        this.endTime = null;
    }


    /**
     * Erzeugt neuen Zeitbereich.
     *
     * @param  minTime  Startzeit.
     * @param  maxTime  Endzeit.
     */
    public TimeRange(long minTime, long maxTime) {
        this(new Date(minTime), new Date(maxTime));
    }


    /**
     * Erzeugt neuen Zeitbereich.
     *
     * @param  beginTime  Startzeit.
     * @param  endTime    Endzeit.
     */
    @Builder
    public TimeRange(Date beginTime, Date endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }


    /**
     * Erzeugt neuen Zeitbereich.
     *
     * @param  beginTime  Startzeit.
     * @param  endTime    Endzeit.
     */
    public TimeRange(Instant beginTime, Instant endTime) {
        this(Date.from(beginTime), Date.from(endTime));
    }


    /**
     * Erzeugt neuen Zeitbereich.
     *
     * @param  beginTime  Startzeit.
     * @param  endTime    Endzeit.
     */
    public TimeRange(ZonedDateTime beginTime, ZonedDateTime endTime) {
        this(beginTime.toInstant(), endTime.toInstant());
    }


    /**
     * Gibt den LongRange basierend auf <code>minTime</code> und <code>maxTime</code> zur&uuml;ck.
     *
     * @return  LongRange.
     */
    private Range<Long> getLongRange() {
        if (longRange == null) {
            longRange = Range.between(getMinTime(), getMaxTime() - 1);
        }
        return longRange;
    }


    /**
     * Testet, ob die Zeit <code>time</code> im Zeitbereich liegt.
     *
     * @param   time  Zeit.
     *
     * @return  <code>true</code>, wenn <code>time</code> im Zeitbereich liegt.
     */
    public boolean containsTime(long time) {
        return getLongRange().contains(time);
    }


    /**
     * Testet, ob die Zeit <code>time</code> im Zeitbereich liegt.
     *
     * @param   time  Zeit.
     *
     * @return  <code>true</code>, wenn <code>time</code> im Zeitbereich liegt.
     */
    public boolean containsTime(Date time) {
        return containsTime(time.getTime());
    }


    /**
     * Testet, ob der Zeitbereich <code>tr</code> innerhalb dieses Zeitbereichs liegt. Der Zeitbereich liegt innerhalb
     * dieses Zeitbereiches, wenn <code>minTime &lt;= tr.minTime &lt; maxTime AND minTime &lt;= tr.maxTime &lt; maxTime</code>.
     *
     * @param   tr  Zeitbereich,
     *
     * @return  <code>true</code>, wenn <code>tr</code> innerhalb dieses Zeitbereiches liegt. Sonst <code>false</code>.
     */
    public boolean containsRange(TimeRange tr) {
        return getLongRange().containsRange(tr.getLongRange());
    }


    /**
     * Gibt die Anfangszeit (inclusive) des Zeitintervalles an.
     *
     * @return  Anfangszeit (inclusive) des Zeitintervalles.
     */
    public long getMinTime() {
        return beginTime.getTime();
    }


    /**
     * Gibt die Endzeit (exclusive) des Zeitintervalles an.
     *
     * @return  Endzeit (exclusive) des Zeitintervalles.
     */
    public long getMaxTime() {
        return endTime.getTime();
    }


    /**
     * Gibt die Anfangszeit (inclusive) des Zeitintervalles an. Identisch mit {@link #getBeginTime()}
     *
     * @return  Anfangszeit (inclusive) des Zeitintervalles.
     */
    public Date getMinDate() {
        return getBeginTime();
    }


    /**
     * Gibt die Endzeit (exclusive) des Zeitintervalles an. Identisch mit {@link #getEndTime()}
     *
     * @return  Endzeit (exclusive) des Zeitintervalles.
     */
    public Date getMaxDate() {
        return getEndTime();
    }


    /**
     * Gibt die Anfangszeit (inclusive) des Zeitintervalles an. Identisch mit {@link #getBeginTime()}
     *
     * @return  Anfangszeit (inclusive) des Zeitintervalles.
     */
    public Instant getMinInstant() {
        return getBeginTime().toInstant();
    }


    /**
     * Gibt die Endzeit (exclusive) des Zeitintervalles an. Identisch mit {@link #getEndTime()}
     *
     * @return  Endzeit (exclusive) des Zeitintervalles.
     */
    public Instant getMaxInstant() {
        return getEndTime().toInstant();
    }


    /**
     * Gibt einen String im Format <code>minDate/maxDate</code> zur&uuml;ck.
     *
     * @return  Textdarstellung des Zeitbereiches.
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0}/{1}", DateUtil.toInstant(beginTime), DateUtil.toInstant(endTime));
    }


    /**
     * Gibt die Anzahl Millisekunden zurueck, die der Zeitbereich umfasst. Dies ist
     *
     * <pre>
     * timerange.getMaxTime() - timerange.getMinTime()
     * </pre>
     *
     * @return  Anzahl Millisekunden, die der Zeitbereich umfasst.
     */
    public long millis() {
        return getMaxTime() - getMinTime();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 25).append(getMinTime()).append(getMaxTime()).toHashCode();
    }


    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj == this) {
            result = true;
        } else if (obj == null || obj.getClass() != TimeRange.class) {
            result = false;
        } else {
            final TimeRange o = (TimeRange) obj;
            result = new EqualsBuilder().append(getMinTime(), o.getMinTime()).append(getMaxTime(), o.getMaxTime())
                    .isEquals();
        }

        return result;
    }


    /**
     * Testet, ob sich die Zeitbereiche ueberschneiden.
     * @param other Der andere Zeitbereich.
     * @return <code>true</code>, wenn sich die beiden Zeitbereiche ueberschneiden, sonst <code>false</code>.
     */
    public boolean isOverlappedBy(TimeRange other) {
        Objects.requireNonNull(other.getLongRange(), SimpleTextSupplier.argMustNotBeNull("other"));
        return this == other
                ? true
                : getLongRange().isOverlappedBy(other.getLongRange());
    }


    /**
     * Ermittelt den Zeitbereich, in dem sich die beiden Zeitbereich ueberschneiden.
     * @param other Der andere Zeitbereich.
     * @return Zeitbereich der Ueberschneidung
     * @throws IllegalArgumentException Wenn sich die beiden Zeitbereiche nicht ueberschneiden.
     * 
     * @see #isOverlappedBy(TimeRange)
     */
    public TimeRange intersect(TimeRange other) throws IllegalArgumentException {
        TimeRange result;

        Objects.requireNonNull(other.getLongRange(), SimpleTextSupplier.argMustNotBeNull("other"));
        if (this == other) {
            result = this;
        } else {
            Range<Long> intersection = getLongRange().intersectionWith(other.getLongRange());

            result = new TimeRange(intersection.getMinimum(), intersection.getMaximum() + 1);
        }

        return result;
    }


    /**
     * Ermittelt den Zeitbereich, in dem sich die beiden Zeitbereich ueberschneiden.
     * @param other Der andere Zeitbereich.
     * @param defaultValue Zeitbereich, der zurueckgegeben wird, falls keine Ueberschneidung besteht.
     * @return Zeitbereich der Ueberschneidung oder {@code defaultValue}.
     * 
     * @see #isOverlappedBy(TimeRange)
     */
    public TimeRange intersect(TimeRange other, TimeRange defaultValue) throws IllegalArgumentException {
        try {
            return intersect(other);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

}
