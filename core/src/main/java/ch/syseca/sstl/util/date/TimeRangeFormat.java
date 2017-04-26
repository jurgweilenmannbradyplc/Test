package ch.syseca.sstl.util.date;


import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;


public class TimeRangeFormat extends Format {

    private static final DateTimeFormatter ISO_DATE_TIME_WITH_MILLIS;
    private static final DateTimeFormatter ISO_DATE_TIME_NO_MILLIS;
    private static final DateTimeFormatter ISO_DATE_TIME_NO_SECONDS;


    static {
        ISO_DATE_TIME_WITH_MILLIS = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral('.')
                .appendValue(MILLI_OF_SECOND, 3)
                .appendZoneId()
                .toFormatter();

        ISO_DATE_TIME_NO_MILLIS = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendZoneId()
                .toFormatter();

        ISO_DATE_TIME_NO_SECONDS = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendZoneId()
                .toFormatter();
    }

    private final DateTimeFormatter formatter;


    /**
     * Returns a formatter that formats the TimeRange in ISO format. The format is
     * {@code yyyy-MM-ddTHH:mm:ss.SSSZ/yyyy-MM-ddTHH:mm:ss.SSSZ}.
     */
    public TimeRangeFormat() {
        this(ISO_DATE_TIME_WITH_MILLIS);
    }


    private TimeRangeFormat(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }


    /**
     * 
     * @see TimeRangeFormat#TimeRangeFormat()
     */
    public static TimeRangeFormat getISOFormatWithMillis() {
        return new TimeRangeFormat();
    }


    /**
     * Returns a formatter that formats the TimeRange in ISO format. The format is {@code yyyy-MM-ddTHH:mm:ssZ/yyyy-MM-ddTHH:mm:ssZ}.
     * 
     * @see ISODateTimeFormat#dateHourMinuteSecond()
     */
    public static TimeRangeFormat getISOFormatNoMillis() {
        return new TimeRangeFormat(ISO_DATE_TIME_NO_MILLIS);
    }


    /**
     * Returns a formatter that formats the TimeRange in ISO format. The format is {@code yyyy-MM-ddTHH:mmZ/yyyy-MM-ddTHH:mmZ}.
     * 
     * @see ISODateTimeFormat#dateHourMinuteSecondMillis()
     */
    public static TimeRangeFormat getISOFormatNoSeconds() {
        return new TimeRangeFormat(ISO_DATE_TIME_NO_SECONDS);
    }


    public String formatTimeRange(TimeRange timerange) {
        return super.format(timerange);
    }


    public StringBuffer formatTimeRange(TimeRange timerange, StringBuffer toAppendTo, FieldPosition pos) {
        return format(timerange, toAppendTo, pos);
    }


    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        TimeRange tr = (TimeRange) obj;

        toAppendTo.append(formatter.format(ZonedDateTime.ofInstant(tr.getMinDate().toInstant(), ZoneOffset.UTC)));
        toAppendTo.append('/');
        toAppendTo.append(formatter.format(ZonedDateTime.ofInstant(tr.getMaxDate().toInstant(), ZoneOffset.UTC)));

        return toAppendTo;
    }


    @Override
    public TimeRange parseObject(String source) throws ParseException {
        return (TimeRange) super.parseObject(source);
    }


    @Override
    public TimeRange parseObject(String source, ParsePosition pos) {
        String s;
        String[] dateStrings;
        Instant d1;
        Instant d2;

        s = pos.getIndex() == 0 ? source : source.substring(pos.getIndex());
        dateStrings = s.split("/");

        d1 = Instant.from(formatter.parse(dateStrings[0]));
        d2 = Instant.from(formatter.parse(dateStrings[1]));

        pos.setIndex(source.length());

        return new TimeRange(Date.from(d1), Date.from(d2));
    }

}
