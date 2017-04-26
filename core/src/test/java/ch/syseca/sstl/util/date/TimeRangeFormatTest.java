package ch.syseca.sstl.util.date;


import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.junit.BeforeClass;
import org.junit.Test;


public class TimeRangeFormatTest {

    private static SimpleDateFormat df;


    @BeforeClass
    public static void init() throws Exception {
        df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    private TimeRange createTimeRange(String from, String to) {
        TimeRange r;

        try {
            r = new TimeRange(df.parse(from), df.parse(to));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }

        return r;
    }


    @Test
    public void formatISOWithMillis() {
        assertEquals(
                "2012-02-01T14:30:00.123Z/2012-12-11T14:30:00.456Z",
                TimeRangeFormat.getISOFormatWithMillis().formatTimeRange(
                        createTimeRange("01.02.2012 14:30:00.123", "11.12.2012 14:30:00.456")));
        assertEquals(
                "2012-02-01T14:30:10.000Z/2012-12-11T14:30:10.000Z",
                TimeRangeFormat.getISOFormatWithMillis().formatTimeRange(
                        createTimeRange("01.02.2012 14:30:10.000", "11.12.2012 14:30:10.000")));
    }


    @Test
    public void formatISONoMillis() {
        assertEquals(
                "2012-02-01T14:30:00Z/2012-12-11T14:30:00Z",
                TimeRangeFormat.getISOFormatNoMillis().formatTimeRange(
                        createTimeRange("01.02.2012 14:30:00.123", "11.12.2012 14:30:00.456")));
    }


    @Test
    public void formatISONoSeconds() {
        assertEquals(
                "2012-02-01T14:30Z/2012-12-11T14:30Z",
                TimeRangeFormat.getISOFormatNoSeconds().formatTimeRange(
                        createTimeRange("01.02.2012 14:30:00.123", "11.12.2012 14:30:00.456")));
    }


    @Test
    public void parseISO() throws ParseException {
        TimeRange t1 = createTimeRange("01.02.2012 14:30:01.123", "11.12.2012 14:30:02.000");
        TimeRange t2 = TimeRangeFormat.getISOFormatWithMillis().parseObject(
                "2012-02-01T14:30:01.123Z/2012-12-11T14:30:02.000Z");

        assertEquals(t1, t2);
    }


    @Test
    public void parseISONoMillis() throws ParseException {
        TimeRange t1 = createTimeRange("01.02.2012 14:30:01.000", "11.12.2012 14:30:02.000");
        TimeRange t2 = TimeRangeFormat.getISOFormatNoMillis().parseObject("2012-02-01T14:30:01Z/2012-12-11T14:30:02Z");

        assertEquals(t1, t2);
    }


    @Test
    public void parseISONoSeconds() throws ParseException {
        TimeRange t1 = createTimeRange("01.02.2012 14:30:00.000", "11.12.2012 14:30:00.000");
        TimeRange t2 = TimeRangeFormat.getISOFormatNoSeconds().parseObject("2012-02-01T14:30Z/2012-12-11T14:30Z");

        assertEquals(t1, t2);
    }
}
